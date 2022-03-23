package io.buildbright.doken;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DokenApplication {

	public static void detectTextGcs() throws IOException {
		// TODO(developer): Replace these variables before running the sample.
		// String filePath = "https://buildbright.io/rsc/wakeupcat.jpg";
		String filePath = "https://buildbright.io/rsc/jpn.png";
		detectTextGcs(filePath);
	}
	
	// Detects text in the specified remote image on Google Cloud Storage.
	public static void detectTextGcs(String gcsPath) throws IOException {
		List<AnnotateImageRequest> requests = new ArrayList<>();

		ImageSource imgSource = ImageSource.newBuilder().setImageUri(gcsPath).build();
		Image img = Image.newBuilder().setSource(imgSource).build();
		Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
		AnnotateImageRequest request =
			AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);

		// Initialize client that will be used to send requests. This client only needs to be created
		// once, and can be reused for multiple requests. After completing all of your requests, call
		// the "close" method on the client to safely clean up any remaining background resources.
		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			for (AnnotateImageResponse res : responses) {
			if (res.hasError()) {
				System.out.format("Error: %s%n", res.getError().getMessage());
				return;
			}

			// For full list of available annotations, see http://g.co/cloud/vision/docs
			for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
				System.out.format("Text: %s%n", annotation.getDescription());
				System.out.format("Position : %s%n", annotation.getBoundingPoly());
			}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		detectTextGcs();

		// Initialize client that will be used to send requests. This client only needs to be created
	  // once, and can be reused for multiple requests. After completing all of your requests, call
	  // the "close" method on the client to safely clean up any remaining background resources.

	//   try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
  
	// 	// The path to the image file to annotate
	// 	String fileName = "./resources/wakeupcat.jpg";
  
	// 	// Reads the image file into memory
	// 	Path path = Paths.get(fileName);
	// 	byte[] data = Files.readAllBytes(path);
	// 	ByteString imgBytes = ByteString.copyFrom(data);
  
	// 	// Builds the image annotation request
	// 	List<AnnotateImageRequest> requests = new ArrayList<>();
	// 	Image img = Image.newBuilder().setContent(imgBytes).build();
	// 	Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
	// 	AnnotateImageRequest request =
	// 		AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
	// 	requests.add(request);
  
	// 	// Performs label detection on the image file
	// 	BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
	// 	List<AnnotateImageResponse> responses = response.getResponsesList();
  
	// 	for (AnnotateImageResponse res : responses) {
	// 	  if (res.hasError()) {
	// 		System.out.format("Error: %s%n", res.getError().getMessage());
	// 		return;
	// 	  }
  
	// 	  for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
	// 		annotation
	// 			.getAllFields()
	// 			.forEach((k, v) -> System.out.format("%s : %s%n", k, v.toString()));
	// 	  }
	// 	}
	//   }

	  // SpringApplication.run(DokenApplication.class, args);
	}
		
	@GetMapping("/hello")
		public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

}