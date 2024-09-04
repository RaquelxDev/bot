package umg.progra2;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import umg.progra2.botTelegram.*;
import umg.progra2.model.Cuestionario;
import umg.progra2.model.User;
import umg.progra2.service.CuestionarioService;
import umg.progra2.service.UserService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    /**
     * Explicación y ejemplo de uso básico de la estructura de datos Map en Java.
     *
     * Un Map es una colección que almacena pares clave-valor.
     * Es útil cuando necesitamos asociar valores con una clave única, como en un diccionario.
     *
     * Ejemplo: Si deseamos almacenar nombres de personas con su número de teléfono,
     * podemos usar un Map donde la clave es el nombre y el valor es el número de teléfono.
     */

    private static void PruebaInsertaUsuario() {
        //explicación:
        // 1. Servicio
        //Servicio (UserService.java):
        //La clase UserService actúa como intermediario entre el controlador y la capa de acceso a datos (DAO).
        // Se encarga de la lógica de negocio, validaciones y de coordinar las transacciones.
        // 2. DAO
        //Capa de Acceso a Datos (UserDao.java):
        //Esta clase contiene los métodos para interactuar con la base de datos, usando la
        // conexión proporcionada por DatabaseConnection. Aquí es donde se construyen y ejecutan
        // las consultas SQL.
        // 3. Conexión a la Base de Datos
        //Gestión de la Conexión (DatabaseConnection.java):
        //Esta clase es responsable de proporcionar la conexión a la base de datos. Puede leer la configuración
        // desde un archivo de propiedades (application.properties) para obtener los detalles de conexión.
        // 4. Transacciones
        //Gestión de Transacciones (TransactionManager.java):
        //Esta clase se encarga de iniciar, confirmar o revertir transacciones en la base de datos.
        // Se utiliza para agrupar varias operaciones en una sola transacción y garantizar la integridad de los datos.
        // 5. Modelo (User.java):
        //La clase User representa la estructura de los datos que se insertan en la base de datos.
        // Es una clase POJO (Plain Old Java Object) con atributos, getters y setters.

        //invoca el servicio que manejará la lógica de negocio.
        UserService userService=new UserService();
        User user = new User();

        // Crear un nuevo usuarioUseruser=newUser();
        user.setCarne("0905-12-12345");
        user.setNombre("Andrea Lopez");
        user.setCorreo("ALopez@gmail.com");
        user.setSeccion("A");
        user.setTelegramid(1234567890L);
        user.setActivo("Y");

        try {
            userService.createUser(user);
            System.out.println("User created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void InsertarCuestionario(){
        CuestionarioService cuestionarioService =new CuestionarioService();
        Cuestionario cuestionario = new Cuestionario();

        // Crear un nuevo usuarioUseruser=newUser();
        cuestionario.setSeccion("SECCION 4");
        cuestionario.setPreguntaid(2);
        cuestionario.setRespuestaTexto("16");
        cuestionario.setTelegramid(123456789L);


        try {
            cuestionarioService.crearUsuario(cuestionario);
            System.out.println("User created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void PruebaActualizacionUsuario() {
        UserService servicioUsuaio = new UserService();

        User usurioObtenido;
        //obtener información del usuario por correo electrónico
        try {
            usurioObtenido = servicioUsuaio.getUserByCarne("0905-12-12345");
            System.out.println("Retrieved User: " + usurioObtenido.getNombre());
            System.out.println("Retrieved User: " + usurioObtenido.getCorreo());
            System.out.println("Retrieved User: " + usurioObtenido.getId());

            //actualizar información del usuario
            usurioObtenido.setCarne("0905-12-12345");
            usurioObtenido.setNombre("Andreita Ascoli");
            usurioObtenido.setCorreo("anAscoli@gmail.com");
            usurioObtenido.setSeccion("A");
            usurioObtenido.setTelegramid(1234567890L);
            usurioObtenido.setActivo("Y");

            servicioUsuaio.updateUser(usurioObtenido);
            System.out.println("User updated successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void PruebaEliminarUsuario() {
        UserService servicioUsuaio = new UserService();
        try {
            servicioUsuaio.deleteUserByEmail("anAscoli@gmail.com");
            System.out.println("User deleted successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void PruebaGetnUsuario() {
        UserService servicioUsuaio = new UserService();

        User usurioObtenido;
        //obtener información del usuario por correo electrónico
        try {
            usurioObtenido = servicioUsuaio.getUserByCarne("0905-12-12345");
            System.out.println("Retrieved User: " + usurioObtenido.getNombre());
            System.out.println("Retrieved User: " + usurioObtenido.getCorreo());
            System.out.println("Retrieved User: " + usurioObtenido.getId());
            System.out.println("User updated successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Explicación y ejemplo de uso básico de la estructura de datos Map en Java.
     *
     * Un Map es una colección que almacena pares clave-valor.
     * Es útil cuando necesitamos asociar valores con una clave única, como en un diccionario.
     *
     * Ejemplo: Si deseamos almacenar nombres de personas con su número de teléfono,
     * podemos usar un Map donde la clave es el nombre y el valor es el número de teléfono.
     */
    public static void explicacionUsoMap1() {
        // Creación de un HashMap, que es una implementación común de Map.
        Map<String, String> phoneBook = new HashMap<>();

        // 1. Insertar elementos en el Map usando el método put.
        phoneBook.put("Alice", "123-4567");
        phoneBook.put("Bob", "987-6543");
        phoneBook.put("Charlie", "555-7890");

        // 2. Recuperar un valor a partir de una clave usando el método get.
        String bobPhoneNumber = phoneBook.get("Bob");
        System.out.println("El número de Bob es: " + bobPhoneNumber);

        // 3. Comprobar si una clave existe en el Map.
        if (phoneBook.containsKey("Alice")) {
            System.out.println("El número de Alice es: " + phoneBook.get("Alice"));
        }

        // 4. Recorrer un Map usando un bucle for-each.
        // Se pueden recorrer las claves o los valores.
        System.out.println("\nLista completa de contactos:");
        for (Map.Entry<String, String> entry : phoneBook.entrySet()) {
            System.out.println("Nombre: " + entry.getKey() + ", Número: " + entry.getValue());
        }

        // 5. Eliminar un elemento del Map.
        phoneBook.remove("Charlie");
        System.out.println("\nDespués de eliminar a Charlie, la lista es:");
        for (Map.Entry<String, String> entry : phoneBook.entrySet()) {
            System.out.println("Nombre: " + entry.getKey() + ", Número: " + entry.getValue());
        }

        // 6. Tamaño del Map (número de pares clave-valor).
        System.out.println("\nEl número total de contactos es: " + phoneBook.size());
    }

    public static void main(String[] args){
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botCuestionario Botcuestionario = new botCuestionario();
            //botPregunton Botpregunton = new botPregunton();
            botsApi.registerBot(Botcuestionario);
            System.out.println("El bot esta funcionando...");
        }
        catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
        }





//        InsertarCuestionario();
//        PruebaInsertaUsuario();
//        PruebaActualizacionUsuario();
//        PruebaGetnUsuario();
//        explicacionUsoMap1();

    }




}