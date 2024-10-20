package http_classes.kucoin;
import org.json.JSONObject;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class ApiForWebsocketConn {
    private static final String API_URL = "https://api.kucoin.com/api/v1/bullet-public";

    public static String sendPostRequest() throws Exception {
        // Создание объекта URL
        URL url = new URL(API_URL);
        // Открытие соединения
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");  // Устанавливаем тип контента
        connection.setDoOutput(true);  // Указываем, что будем отправлять данные

        // Если требуется отправить данные в теле запроса, например, JSON
        String jsonInputString = "{}"; // В данном случае тело запроса пустое

        // Отправка данных
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Чтение ответа
        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();
        if (responseCode == HttpURLConnection.HTTP_OK) { // 200
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = in.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
        } else {
            System.out.println("POST request failed with response code: " + responseCode);
        }

        // Закрываем соединение
        connection.disconnect();


        JSONObject jsonObject= new JSONObject(response.toString());
        JSONObject dataObject = jsonObject.getJSONObject("data");
        String token = dataObject.getString("token");

        return token.toString(); // Возвращаем ответ
    }

}
