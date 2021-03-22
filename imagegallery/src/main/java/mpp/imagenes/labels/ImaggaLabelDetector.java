package mpp.imagenes.labels;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class ImaggaLabelDetector implements LabelDetector {

	public static void main(String[] args) throws IOException {
		String f = 	"<ruta-a-tu-imagen>";
		new ImaggaLabelDetector().label(new File(f));
	}
	
	private void setAuth(HttpURLConnection connection) {
		String apiKey = System.getenv("IMAGGA_API_KEY");
		String apiSecret = System.getenv("IMAGGA_API_SECRET");

		String credentialsToEncode = apiKey + ":" + apiSecret;
		String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

		connection.setRequestProperty("Authorization", "Basic " + basicAuth);

	}


	@Override
	public List<Label> label(File f) throws IOException {
		String id = upload(f);
		System.out.println("Uploaded " + id);
		
		

		String endpoint_url = "https://api.imagga.com/v2/tags";

		String url = endpoint_url + "?image_upload_id=" + id;
		URL urlObject = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
		setAuth(connection);

		int responseCode = connection.getResponseCode();

		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		// Formato de la respuesta:
		// {"result":{"tags":[{"confidence":41.4293479919434,"tag":{"en":"daughter"}},{"confidence":39.4777679443359,"tag":{"en":"happy"}},{"confidence":37.4150276184082,"tag":{"en":"outdoors"}},{"confidence":34.0251617431641,"tag":{"en":"people"}},{"confidence":30.7697849273682,"tag":{"en":"love"}},{"confidence":29.2501068115234,"tag":{"en":"grass"}},{"confidence":28.9924907684326,"tag":{"en":"happiness"}},{"confidence":28.8230037689209,"tag":{"en":"child"}},{"confidence":28.4837512969971,"tag":{"en":"adult"}},{"confidence":28.460132598877,"tag":{"en":"family"}},{"confidence":28.2952480316162,"tag":{"en":"summer"}},{"confidence":28.2126140594482,"tag":{"en":"smiling"}},{"confidence":27.9930286407471,"tag":{"en":"park"}},{"confidence":27.8712596893311,"tag":{"en":"couple"}},{"confidence":27.0794734954834,"tag":{"en":"smile"}},{"confidence":26.2840270996094,"tag":{"en":"together"}},{"confidence":26.0816135406494,"tag":{"en":"flowers"}},{"confidence":25.668909072876,"tag":{"en":"outside"}},{"confidence":24.4720153808594,"tag":{"en":"bouquet"}},{"confidence":24.0887355804443,"tag":{"en":"garden"}},{"confidence":22.8102149963379,"tag":{"en":"darling"}},{"confidence":22.5741271972656,"tag":{"en":"groom"}},{"confidence":22.4011707305908,"tag":{"en":"lifestyle"}},{"confidence":22.3555564880371,"tag":{"en":"mother"}},{"confidence":22.1686725616455,"tag":{"en":"outdoor"}},{"confidence":22.1672325134277,"tag":{"en":"person"}},{"confidence":22.000675201416,"tag":{"en":"portrait"}},{"confidence":21.5898113250732,"tag":{"en":"male"}},{"confidence":20.8303050994873,"tag":{"en":"man"}},{"confidence":19.4791145324707,"tag":{"en":"two"}},{"confidence":19.1796798706055,"tag":{"en":"bride"}},{"confidence":17.5382251739502,"tag":{"en":"joy"}},{"confidence":17.4939498901367,"tag":{"en":"parent"}},{"confidence":17.2141017913818,"tag":{"en":"fun"}},{"confidence":17.1631317138672,"tag":{"en":"dress"}},{"confidence":17.0657482147217,"tag":{"en":"cheerful"}},{"confidence":16.148006439209,"tag":{"en":"flower"}},{"confidence":16.1182479858398,"tag":{"en":"childhood"}},{"confidence":15.6340351104736,"tag":{"en":"wedding"}},{"confidence":15.021541595459,"tag":{"en":"women"}},{"confidence":14.942774772644,"tag":{"en":"leisure"}},{"confidence":14.693510055542,"tag":{"en":"pretty"}},{"confidence":14.5346536636353,"tag":{"en":"mom"}},{"confidence":14.3499879837036,"tag":{"en":"cute"}},{"confidence":14.2437763214111,"tag":{"en":"marriage"}},{"confidence":13.9917125701904,"tag":{"en":"attractive"}},{"confidence":13.5537080764771,"tag":{"en":"casual"}},{"confidence":13.4212226867676,"tag":{"en":"married"}},{"confidence":13.2308712005615,"tag":{"en":"laughing"}},{"confidence":13.1226835250854,"tag":{"en":"senior"}},{"confidence":13.0384016036987,"tag":{"en":"boy"}},{"confidence":13.0171127319336,"tag":{"en":"mature"}},{"confidence":12.7591819763184,"tag":{"en":"girls"}},{"confidence":12.5574464797974,"tag":{"en":"meadow"}},{"confidence":12.404842376709,"tag":{"en":"kid"}},{"confidence":12.0247468948364,"tag":{"en":"sitting"}},{"confidence":11.7735900878906,"tag":{"en":"spring"}},{"confidence":11.5076637268066,"tag":{"en":"active"}},{"confidence":11.4481420516968,"tag":{"en":"husband"}},{"confidence":11.2463474273682,"tag":{"en":"human"}},{"confidence":11.2399272918701,"tag":{"en":"father"}},{"confidence":10.706015586853,"tag":{"en":"healthy"}},{"confidence":10.6528034210205,"tag":{"en":"face"}},{"confidence":10.4285936355591,"tag":{"en":"wife"}},{"confidence":10.4243202209473,"tag":{"en":"hands"}},{"confidence":10.2687158584595,"tag":{"en":"son"}},{"confidence":10.0106248855591,"tag":{"en":"supporter"}},{"confidence":10.0027675628662,"tag":{"en":"relaxing"}},{"confidence":9.90213584899902,"tag":{"en":"holding"}},{"confidence":9.78933906555176,"tag":{"en":"grandmother"}},{"confidence":9.57140350341797,"tag":{"en":"home"}},{"confidence":9.48172664642334,"tag":{"en":"gardening"}},{"confidence":9.47812366485596,"tag":{"en":"play"}},{"confidence":9.46114063262939,"tag":{"en":"adults"}},{"confidence":9.43765830993652,"tag":{"en":"togetherness"}},{"confidence":9.3682165145874,"tag":{"en":"relationship"}},{"confidence":9.23784351348877,"tag":{"en":"camera"}},{"confidence":9.20381546020508,"tag":{"en":"cauliflower"}},{"confidence":9.20046043395996,"tag":{"en":"field"}},{"confidence":9.1869535446167,"tag":{"en":"joyful"}},{"confidence":9.1083402633667,"tag":{"en":"children"}},{"confidence":9.03370952606201,"tag":{"en":"school"}},{"confidence":8.95839786529541,"tag":{"en":"one"}},{"confidence":8.95668029785156,"tag":{"en":"activity"}},{"confidence":8.60921764373779,"tag":{"en":"sunny"}},{"confidence":8.53778743743896,"tag":{"en":"hobby"}},{"confidence":8.50169944763184,"tag":{"en":"life"}},{"confidence":8.49666690826416,"tag":{"en":"grandma"}},{"confidence":8.37107563018799,"tag":{"en":"relaxation"}},{"confidence":8.28850746154785,"tag":{"en":"sky"}},{"confidence":8.22812747955322,"tag":{"en":"care"}},{"confidence":8.22118091583252,"tag":{"en":"countryside"}},{"confidence":8.21814060211182,"tag":{"en":"vegetable"}},{"confidence":8.0359354019165,"tag":{"en":"romance"}},{"confidence":7.97687149047852,"tag":{"en":"blond"}},{"confidence":7.80995941162109,"tag":{"en":"gown"}},{"confidence":7.67377901077271,"tag":{"en":"edible fruit"}},{"confidence":7.66005229949951,"tag":{"en":"elderly"}},{"confidence":7.57441473007202,"tag":{"en":"enjoying"}},{"confidence":7.53555297851562,"tag":{"en":"horizontal"}},{"confidence":7.53379392623901,"tag":{"en":"kids"}},{"confidence":7.50978422164917,"tag":{"en":"relaxed"}},{"confidence":7.49454402923584,"tag":{"en":"produce"}},{"confidence":7.37347221374512,"tag":{"en":"emotion"}},{"confidence":7.29298162460327,"tag":{"en":"playing"}},{"confidence":7.22325325012207,"tag":{"en":"clothing"}},{"confidence":7.19596004486084,"tag":{"en":"looking"}},{"confidence":7.12472057342529,"tag":{"en":"romantic"}},{"confidence":7.09948062896729,"tag":{"en":"plant"}},{"confidence":7.06679916381836,"tag":{"en":"little"}},{"confidence":7.06031274795532,"tag":{"en":"day"}}]},"status":{"text":"","type":"success"}}
		
		TreeMap<Double, List<String>> results = new TreeMap<>();
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode json = objectMapper.readTree(connection.getInputStream());
		JsonNode r = json.get("result");
		if (r != null) {
			JsonNode tags = r.get("tags");
			if (tags instanceof ArrayNode) {
				ArrayNode array = (ArrayNode) tags;
				for (JsonNode jsonNode : array) {
					System.out.println(jsonNode);
					double confidence = jsonNode.get("confidence").asDouble();
					JsonNode tag = jsonNode.get("tag");
					if (tag == null)
						continue;

					JsonNode en_ = tag.get("en");
					if (en_ == null)
						continue;
					
					String tagEn = en_.asText();
					results.computeIfAbsent(confidence, (k) -> new ArrayList<>());
					results.get(confidence).add(tagEn);
					
					System.out.println("Found tag: " + tagEn);
				}
			}
		}
		
		List<Label> result = new ArrayList<>();
		LOOP: for (Double key : results.descendingKeySet()) {
			List<String> tags = results.get(key);
			for (String tag : tags) {
				result.add(new Label(tag, key));
				if (result.size() == 10)
					break LOOP;
			}
		}
		
		return result;
		
		
	}

	public String upload(File fileToUpload) throws IOException {
		String endpoint = "/uploads";

		String crlf = "\r\n";
		String twoHyphens = "--";
		String boundary = "Image Upload";

		URL urlObject = new URL("https://api.imagga.com/v2" + endpoint);
		HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
		setAuth(connection);
		connection.setUseCaches(false);
		connection.setDoOutput(true);

		connection.setRequestMethod("POST");
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("Cache-Control", "no-cache");
		connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

		DataOutputStream request = new DataOutputStream(connection.getOutputStream());

		request.writeBytes(twoHyphens + boundary + crlf);
		request.writeBytes(
				"Content-Disposition: form-data; name=\"image\";filename=\"" + fileToUpload.getName() + "\"" + crlf);
		request.writeBytes(crlf);

		try (InputStream inputStream = new FileInputStream(fileToUpload)) {
			int bytesRead;
			byte[] dataBuffer = new byte[1024];
			while ((bytesRead = inputStream.read(dataBuffer)) != -1) {
				request.write(dataBuffer, 0, bytesRead);
			}

			request.writeBytes(crlf);
			request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
			request.flush();
			request.close();

			InputStream responseStream = new BufferedInputStream(connection.getInputStream());
			// Example response:
			//   - {"result":{"upload_id":"i1475833eebb07941a3dd9468b4tnv3y"},"status":{"text":"","type":"success"}}
			
			

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode json = objectMapper.readTree(responseStream);
			JsonNode r = json.get("result");
			if (r != null) {
				JsonNode value = r.get("upload_id");
				if (value != null) {
					return value.asText();
				}
			}
			
			responseStream.close();
			connection.disconnect();
			
			return null; //response;
		}
	}

}
