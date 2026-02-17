import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class EjemploPersonaSerializable{
    public static void main(String[] args){

        Persona persona = new Persona("Juan", 30);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("persona.ser"))) {
            oos.writeObject(persona);
            System.out.println("Objeto Persona serializado y guardado en persona.ser");
        } catch (IOException e) {
            System.out.println("Error al guardar el objeto: " +
                    e.getMessage());
        }
    }
}