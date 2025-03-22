package be.pxl.mousselsouper_backend;


import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/payments")
public class MollieController {

    @Value("${mollie.api.key}")
    private String mollieApiKey;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@org.springframework.web.bind.annotation.RequestBody PaymentRequest request) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String jsonBody = "{\"amount\":{\"currency\":\"EUR\",\"value\":\"" + request.amount + "\"},\"description\":\"Mosselsouper order\",\"redirectUrl\":\"http://localhost:4200/thank-you\"}";
        RequestBody body = RequestBody.create(jsonBody, mediaType);

        Request httpRequest = new Request.Builder()
                .url("https://api.mollie.com/v2/payments")
                .post(body)
                .addHeader("Authorization", "Bearer " + mollieApiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(httpRequest).execute();
        return ResponseEntity.ok(response.body().string());
    }
}
