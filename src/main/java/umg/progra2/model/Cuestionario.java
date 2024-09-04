package umg.progra2.model;

public class Cuestionario {

    private Long id;  // Opcional, si tienes un campo 'id' en la base de datos
    private String seccion;
    private Long telegramid;
    private String respuestaTexto;  // Alineado con 'respuesta_texto'
    private Integer preguntaid;
    private String nombreid;

    // Getters y Setters para cada campo

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public Long getTelegramid() {
        return telegramid;
    }

    public void setTelegramid(Long telegramid) {
        this.telegramid = telegramid;
    }

    public String getRespuestaTexto() {
        return respuestaTexto;
    }

    public void setRespuestaTexto(String respuestaTexto) {
        this.respuestaTexto = respuestaTexto;
    }

    public Integer getPreguntaid() {
        return preguntaid;
    }

    public void setPreguntaid(Integer preguntaid) {
        this.preguntaid = preguntaid;
    }

    public String getNombreid() {
        return nombreid;
    }

    public void setNombreid(String nombreid) {
        this.nombreid = nombreid;
    }
}
