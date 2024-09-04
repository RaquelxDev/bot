package umg.progra2.botTelegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import umg.progra2.model.Cuestionario;
import umg.progra2.model.User;
import umg.progra2.service.CuestionarioService;
import umg.progra2.service.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class botCuestionario extends TelegramLongPollingBot {

    private Map<Long, String> estadoConversacion = new HashMap<>();
    User usuarioConectado = null;
    UserService userService = new UserService();
    private final Map<Long, Integer> indicePregunta = new HashMap<>();
    private final Map<Long, String> seccionActiva = new HashMap<>();
    private final Map<String, String[]> preguntas = new HashMap<>();
    Boolean enviar = false;

    public botCuestionario() {
        // Inicializa los cuestionarios con las preguntas.
        preguntas.put("SECTION_1", new String[]{"Estas aburrido?", "¿Te bañaste hoy?", "¿cual es tu color favorito?", "¿que haces?"});
        preguntas.put("SECTION_2", new String[]{"¿Cuánto mides?", "¿Donde vives?", "¿Qué día es hoy?", "¿Cuál es tu comida favorita?"});
        preguntas.put("SECTION_3", new String[]{"¿Qué hora es?","¿Te gusta el clima frío?", "¿Cuál es tu comida favorita?", "¿Te gusta el helado?"});
        preguntas.put("SECTION_4", new String[]{"Como estas?","¿Cuantos añoss tienes?", "¿Cuando cumples años?", "¿Te gusta el chocolate?"});
    }

    @Override
    public String getBotUsername() {
        return "@nexus0101_bot";
    }

    @Override
    public String getBotToken() {
        return "7471885234:AAELXfpDvAMnOZu3JDOzkECKodVvs01Bz18";
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Message message = update.getMessage();
            //obtener el nombre y apellido del usuario en una variable
            String userFirstName = update.getMessage().getFrom().getFirstName();
            String userLastName = update.getMessage().getFrom().getLastName();
            String nickName = update.getMessage().getFrom().getUserName();
            long chat_id = update.getMessage().getChatId();

            try {
                String state = estadoConversacion.getOrDefault(chat_id, "");
                usuarioConectado = userService.getUserByTelegramId(chat_id);

                // Verificación inicial del usuario, si usuarioConectado es nullo, significa que no tiene registro de su id de telegram en la tabla
                if (usuarioConectado == null && state.isEmpty()) {
                    sendText(chat_id, "Hola " + formatUserInfo(userFirstName, userLastName) + ", no tienes un usuario registrado en el sistema. Por favor ingresa tu correo electrónico:");
                    estadoConversacion.put(chat_id, "ESPERANDO_CORREO");
                    return;}
                else if (!messageText.equals("/menu") && !seccionActiva.containsKey(chat_id)){
                    sendText(chat_id, "Hola " + formatUserInfo(userFirstName, userLastName) + ", Envia /menu para iniciar el cuestionario");}

                if (messageText.equals("/menu")) {
                    sendMenu(chat_id);
                } else if (seccionActiva.containsKey(chat_id)) {
                    manejaCuestionario(chat_id, messageText,formatUserInfo(userFirstName,userLastName));
                }

                // Manejo del estado ESPERANDO_CORREO
                if (state.equals("ESPERANDO_CORREO")) {
                    processEmailInput(chat_id, messageText);
                    return;
                }
            }catch (Exception e){
                sendText(chat_id, "Ocurrió un error al procesar tu mensaje. Por favor intenta de nuevo.");
            }}

        else if (update.hasCallbackQuery()) { //es una respusta de un boton
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

    private void manejaCuestionario(long chatId, String response,String nombre) {
        String section = seccionActiva.get(chatId);
        int index = indicePregunta.get(chatId);
        if (indicePregunta.get(chatId) == 1 ) {
            int intresponse = Integer.parseInt(response);
            if (intresponse<5){
                sendText(chatId, "Tu respuesta fue: " + response);
                sendText(chatId, "Edad muy corta, Ingrese otra edad");
                enviarPregunta(chatId);
            } else if (intresponse>120) {
                sendText(chatId, "Tu respuesta fue: " + response);
                sendText(chatId,"Edad demasiado grande. Ingrese otra edad");
                enviarPregunta(chatId);
            }else {
                enviarRespuesta(section,index,response,chatId,nombre);
                siguientepregunta(chatId,response,index);
            }
        } else{
            enviarRespuesta(section,index,response,chatId,nombre);
            siguientepregunta(chatId,response,index);
        }
    }

    private void enviarRespuesta(String seccion,Integer preguntaid, String response,Long telegramid,String nombreid) {
        CuestionarioService cuestionarioService =new CuestionarioService();
        Cuestionario cuestionario = new Cuestionario();

        // Crear un nuevo usuarioUseruser=newUser();
        cuestionario.setSeccion(seccion);
        cuestionario.setPreguntaid(preguntaid);
        cuestionario.setRespuestaTexto(response);
        cuestionario.setTelegramid(telegramid);
        cuestionario.setNombreid(nombreid);


        try {
            cuestionarioService.crearUsuario(cuestionario);
            System.out.println("User created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void siguientepregunta(long chatId,String response,int index) {
        sendText(chatId, "Tu respuesta fue: " + response);
        indicePregunta.put(chatId, index + 1);
        enviarPregunta(chatId);
    }

    private String formatUserInfo(String firstName, String lastName) {
        return firstName + " " + lastName + " ";
    }

    //verifica si el usuario está registrado en la tabla con su correo electrónico
    private void processEmailInput(long chat_id, String email) {
        sendText(chat_id, "Recibo su Correo: " + email);
        estadoConversacion.remove(chat_id); // Reset del estado
        try{
            usuarioConectado = userService.getUserByEmail(email);
        } catch (Exception e) {
            System.err.println("Error al obtener el usuario por correo: " + e.getMessage());
            e.printStackTrace();
        }


        if (usuarioConectado == null) {
            sendText(chat_id, "El correo no se encuentra registrado en el sistema, por favor contacte al administrador.");
        } else {
            usuarioConectado.setTelegramid(chat_id);
            try {
                userService.updateUser(usuarioConectado);
            } catch (Exception e) {
                System.err.println("Error al actualizar el usuario: " + e.getMessage());
                e.printStackTrace();
            }

            sendText(chat_id, "Usuario actualizado con éxito!");
        }
    }

    //función para enviar mensajes
    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }
}