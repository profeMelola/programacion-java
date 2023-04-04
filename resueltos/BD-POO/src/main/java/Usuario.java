public class Usuario
{
    private int id;
    private String nombre;
    private String apellidos;

    /**
     * Constructor for objects of class Usuario
     */
    public Usuario()
    {
        this.nombre = "";
        this.apellidos = "";
        this.id = -1;
    }
    public Usuario(int id, String nombre, String apellidos){
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }
    public Usuario(String nombre, String apellidos)
    {
        this(-1, nombre, apellidos);
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public String getApellidos(){
        return apellidos;
    }
    public void setApellidos(String apellidos){
        this.apellidos = apellidos;
    }

    @Override
    public String toString(){
        return "ID: " + id + " NOMBRE: " + nombre + " APELLIDOS: " + apellidos;
    }

}