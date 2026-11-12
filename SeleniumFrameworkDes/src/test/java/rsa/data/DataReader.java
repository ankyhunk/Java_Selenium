package rsa.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import rsa.testcomponents.TestCredentials;

public class DataReader {
	public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {
		String jsonContent=FileUtils.readFileToString(new File(filePath),
				StandardCharsets.UTF_8);
		ObjectMapper mapper=new ObjectMapper();
		List<HashMap<String,String>> data=mapper.readValue(jsonContent,new TypeReference<List<HashMap<String,String>>>(){});
		// Fill in ${TEST_EMAIL} / ${TEST_PASSWORD} from the environment
		data.forEach(row -> row.replaceAll((key, value) -> TestCredentials.resolve(value)));
		return data;
	}
}
