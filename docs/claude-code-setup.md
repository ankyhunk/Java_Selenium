# Claude Code setup

This repository is set up so that [Claude Code](https://docs.claude.com/en/docs/claude-code/overview) works on the framework the way a team member would: it knows how to build and test the project, follows the Page Object conventions, checks its own edits compile, and can be asked to do work straight from a GitHub issue.

Every file below is committed, so anyone who clones the repo and runs `claude` gets the same behaviour.

## At a glance

| File | What it is | Why it's here |
|---|---|---|
| [`CLAUDE.md`](../CLAUDE.md) | Project memory, loaded at the start of every session | Build commands, conventions and known pitfalls Claude can't work out from the code |
| [`.claude/settings.json`](../.claude/settings.json) | Shared permissions and hooks | No prompts for the build loop, no access to credentials, compile check after every edit |
| [`.claude/skills/new-page-object/SKILL.md`](../.claude/skills/new-page-object/SKILL.md) | Custom skill (`/new-page-object`) | The framework's recipe for adding a page, loaded only when needed |
| [`.claude/agents/test-reviewer.md`](../.claude/agents/test-reviewer.md) | Read-only subagent | Reviews tests for flakiness and missing cleanup in its own context |
| [`.github/workflows/claude.yml`](../.github/workflows/claude.yml) | GitHub Actions workflow | Comment `@claude` on an issue or PR and Claude does the work in CI |

```
Selenium_Framework/
├── CLAUDE.md
├── .claude/
│   ├── settings.json
│   ├── skills/new-page-object/SKILL.md
│   └── agents/test-reviewer.md
├── .github/workflows/claude.yml
├── docs/claude-code-setup.md      # this file
└── SeleniumFrameworkDes/          # the Maven project
```

## 1. `CLAUDE.md`: project memory

Claude Code reads `CLAUDE.md` at the start of every session. It holds three things:

- **Commands**: how to build, run every test, or run a single test class. The Maven project sits in a subfolder, so each command passes `-f SeleniumFrameworkDes/pom.xml` and works from the repository root.
- **Conventions**: one Page Object per page extending `AbstractComponent`, explicit waits instead of `Thread.sleep`, tests extending `BaseTest`, and test data in JSON files.
- **Don'ts**: mistakes that are easy to make and hard to spot.

**Design choices**

- **Keep it short.** `CLAUDE.md` is loaded into every session, so every line costs context. It only holds what Claude can't infer by reading the code. Longer procedures belong in skills (see section 3).
- **Write down real pitfalls.** The JUnit rule comes from a real bug in this project. With JUnit 5 on the classpath, Maven Surefire switches to the JUnit Platform provider and silently runs zero TestNG tests, so the build passes while testing nothing. One line here stops it from happening again.

## 2. `.claude/settings.json`: permissions and hooks

### Permissions

```json
"allow": ["Bash(mvn *)", "Bash(git status)", "Bash(git diff *)"],
"deny":  ["Read(./.env)", "Edit(**/PurchaseOrder.json)"]
```

- **Allowed without asking:** Maven commands and read-only git commands. Claude can build, test and inspect changes without a permission prompt each time.
- **Still asks:** everything else, including `git commit` and `git push`. A person approves anything that changes history or leaves the machine.
- **Denied:** reading `.env` and editing `PurchaseOrder.json`, which holds the test account login. Claude can't read secrets or change the test credentials.

Personal overrides go in `.claude/settings.local.json`, which is git-ignored.

### Compile hook

```json
"PostToolUse": [{
  "matcher": "Edit|Write",
  "hooks": [{ "type": "command",
              "command": "mvn -q -f SeleniumFrameworkDes/pom.xml compile 1>&2 || exit 2" }]
}]
```

After every file Claude edits or writes, the hook compiles the project.

- **How the errors reach Claude:** `-q` keeps Maven quiet when the build succeeds. When it fails, `1>&2` sends Maven's error lines to stderr and `exit 2` tells Claude Code to pass them back to Claude. Claude then sees the exact file, line and error, and fixes it in the next step.
- **Why it isn't optional:** this is a hook rather than an instruction in `CLAUDE.md` because hooks always run. Claude can't forget them or decide to skip them.
- **Trade-offs:** it compiles but doesn't run tests. Compiling takes about 15–20 seconds, mostly Maven starting up, while the test suite drives a real browser against a live site and takes minutes, too slow to run after every edit. The cost is that wait after each edit, including edits to non-Java files. Narrowing the matcher or running the Maven daemon (`mvnd`) would cut it.

## 3. Skill: `/new-page-object`

A skill is a packaged procedure that Claude loads only when it's relevant. This one holds the framework's recipe for adding a page:

1. Create the Page Object in `rsa/pageobjects`, extending `AbstractComponent`, with `@FindBy` locators and `PageFactory.initElements`.
2. Add the navigation method on the page that leads to it.
3. Add a TestNG test extending `BaseTest`.
4. Build and run the new test, and fix anything that fails.

Use it by typing `/new-page-object OrderHistoryPage`. Claude also picks it up on its own when you ask for something like "add a page object for the order history page", because the skill's `description` says when to use it.

**Why a skill and not more `CLAUDE.md`:** the recipe only matters when adding a page. As a skill, it costs no context in the sessions where it isn't needed.

## 4. Subagent: `test-reviewer`

A subagent is a separate Claude instance with its own instructions, tools and context window. `test-reviewer` checks tests for:

- `Thread.sleep` and missing explicit waits, the main causes of flaky Selenium tests
- test data hard-coded in the test instead of read from JSON
- missing `@AfterMethod(alwaysRun = true)` cleanup, which leaves browsers open after failures
- assertions that can never fail

It reports findings as `file:line`.

To use it, ask: "Use the test-reviewer subagent on the tests I just changed."

**Design choices**

- **Read-only tools** (`Read`, `Grep`, `Glob`): a reviewer that can edit might "fix" a failing check by weakening the test. With read-only tools, it can only report, and the main session or a person decides what to change.
- **Its own context:** the review starts fresh rather than inheriting the reasoning that produced the code, so it doesn't share its blind spots. It also keeps the main conversation free of review noise.

## 5. GitHub Actions: `@claude` in issues and pull requests

`.github/workflows/claude.yml` runs [`anthropics/claude-code-action`](https://github.com/anthropics/claude-code-action) whenever an issue or PR comment contains `@claude`. For example, open an issue saying "Add a page object for the Orders page", then comment `@claude implement this`. Claude works on a branch in CI and opens a pull request for review.

- **Same rules as locally:** in CI, Claude runs in the checked-out repo and reads the same `CLAUDE.md` and skill, so its work follows the same conventions as a local session.
- **Cost control:** it runs only when someone writes `@claude`, not on every push, and the API key has a spending limit in the Claude Console.
- **Permissions:** the job gets only what it needs to push a branch and comment (`contents`, `pull-requests`, `issues`) plus `id-token` for the action's GitHub authentication. The API key is stored as the `ANTHROPIC_API_KEY` repository secret, never in the file.
- **Setup:** running `/install-github-app` in Claude Code installs the GitHub app and adds the secret.

## Try it yourself

1. **Hook:** ask Claude to add a method to `CartPage` and mistype a variable name. The compile error comes straight back from the hook, and Claude fixes it without being told.
2. **Skill:** run `/new-page-object OrderHistoryPage`. You get a Page Object, a navigation method and a test that builds.
3. **Subagent:** add `Thread.sleep(3000);` to a test, then ask for a review with the `test-reviewer` subagent. It reports the line.
4. **CI:** open an issue, comment `@claude implement this`, and review the pull request it opens.

## What I'd add next

- **A PreToolUse hook that blocks `git push --force`:** a hard rule rather than a prompt.
- **A browser MCP server** (such as Playwright MCP): Claude could open the store and find locators itself instead of guessing from page source.
- **A pull-request workflow that runs the test suite** in headless Chrome, so Claude's PRs are tested before review.
