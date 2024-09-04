package umg.progra2.botTelegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class botPregunton extends TelegramLongPollingBot {
    private final Map<Long, Integer> indicePregunta = new HashMap<>();
    private final Map<Long, String> seccionActiva = new HashMap<>();
    private final Map<String, String[]> preguntas = new HashMap<>();


    @Override
    public String getBotUsername() {
        return "@nexus0101_bot";
    }

    @Override
    public String getBotToken() {
        return "7471885234:AAELXfpDvAMnOZu3JDOzkECKodVvs01Bz18";
    }



    public botPregunton() {
        // Inicializa los cuestionarios con las preguntas.
        preguntas.put("SECTION_1", new String[]{"Estas aburrido?", "¿Te bañaste hoy?", "¿cual es tu color favorito?", "¿que haces?"});
        preguntas.put("SECTION_2", new String[]{"¿Cuánto mides?", "¿Donde vives?", "¿Qué día es hoy?", "¿Cuál es tu comida favorita?"});
        preguntas.put("SECTION_3", new String[]{"¿Qué hora es?","¿Te gusta el clima frío?", "¿Cuál es tu comida favorita?", "¿Te gusta el helado?"});
        preguntas.put("SECTION_4", new String[]{"Como estas?","¿Cuantos añoss tienes?", "¿Cuando cumples años?", "¿Te gusta el chocolate?"});
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/menu")) {
                sendMenu(chatId);
            } else if (seccionActiva.containsKey(chatId)) {
                manejaCuestionario(chatId, messageText);
            }
        } else if (update.hasCallbackQuery()) { //es una respusta de un boton
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            inicioCuestionario(chatId, callbackData);
        }
    }

    private void sendMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Selecciona una sección:");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // Crea los botones del menú
        rows.add(crearFilaBoton("Sección 1", "SECTION_1"));
        rows.add(crearFilaBoton("Sección 2", "SECTION_2"));
        rows.add(crearFilaBoton("Sección 3", "SECTION_3"));
        rows.add(crearFilaBoton("Sección 4", "SECTION_4"));

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private List<InlineKeyboardButton> crearFilaBoton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);
        return row;
    }

    private void inicioCuestionario(long chatId, String section) {
        seccionActiva.put(chatId, section);
        indicePregunta.put(chatId, 0);
        enviarPregunta(chatId);
    }

    private void enviarPregunta(long chatId) {
        String seccion = seccionActiva.get(chatId);
        int index = indicePregunta.get(chatId);
        String[] questions = preguntas.get(seccion);

        if (index < questions.length) {
            sendText(chatId, questions[index]);
        } else {
            sendText(chatId, "¡Has completado el cuestionario!");
            seccionActiva.remove(chatId);
            indicePregunta.remove(chatId);
        }
    }

    private void manejaCuestionario(long chatId, String response) {
        String section = seccionActiva.get(chatId);
        int index = indicePregunta.get(chatId);

        sendText(chatId, "Tu respuesta fue: " + response);
        indicePregunta.put(chatId, index + 1);

        enviarPregunta(chatId);
    }

    private void sendText(Long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
