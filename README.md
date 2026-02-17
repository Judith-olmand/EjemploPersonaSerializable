# 💾 Serialización de Objetos en Java - Ejemplo Persona

Proyecto educativo que demuestra cómo serializar y deserializar objetos en Java usando `ObjectOutputStream` y `ObjectInputStream`.

## 📋 Descripción

Este proyecto muestra el proceso completo de **serialización** (convertir un objeto a bytes y guardarlo en un archivo) y **deserialización** (recuperar un objeto desde un archivo) en Java. Utiliza la clase `Persona` como ejemplo práctico.

## 🎯 Conceptos Clave

### ¿Qué es la Serialización?

**Serialización** es el proceso de convertir un objeto en una secuencia de bytes para:
- 💾 Guardar el objeto en un archivo
- 📡 Transmitir el objeto por red
- 🗄️ Almacenar el objeto en una base de datos

**Deserialización** es el proceso inverso: reconstruir el objeto desde los bytes.

### ¿Por qué usar Serialización?

- ✅ Persistencia de datos entre ejecuciones del programa
- ✅ Comunicación entre aplicaciones (RMI, sockets)
- ✅ Caché de objetos complejos
- ✅ Implementación de sistemas de undo/redo

## 🏗️ Estructura del Proyecto

```
├── Persona.java                    # Clase modelo (Serializable)
├── EjemploPersonaSerializable.java # Serializa y guarda el objeto
├── LeerPersona.java                # Lee y deserializa el objeto
├── persona.ser                     # Archivo binario generado (output)
└── README.md                       # Este archivo
```

## 📄 Archivos del Proyecto

### 1. Persona.java - Clase Modelo

```java
import java.io.Serializable;

public class Persona implements Serializable {
    private String nombre;
    private int edad;
    
    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }
    
    public int getEdad() {
        return edad;
    }
}
```

**Puntos clave:**
- ✅ Implementa la interfaz `Serializable` (obligatorio)
- ✅ `Serializable` es una interfaz marcadora (sin métodos)
- ✅ Todos los atributos deben ser serializables

### 2. EjemploPersonaSerializable.java - Escritura

```java
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class EjemploPersonaSerializable{
    public static void main(String[] args){
        Persona persona = new Persona("Juan", 30);

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("persona.ser"))) {
            oos.writeObject(persona);
            System.out.println("Objeto Persona serializado");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

**Proceso:**
1. Crea un objeto `Persona`
2. Abre un `ObjectOutputStream` conectado a un archivo
3. Escribe el objeto con `writeObject()`
4. Cierra automáticamente con try-with-resources

### 3. LeerPersona.java - Lectura

```java
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LeerPersona {
    public static void main(String[] args) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("persona.ser"))) {
            Persona persona = (Persona) ois.readObject();
            System.out.println("Nombre: " + persona.getNombre() + 
                             ", Edad: " + persona.getEdad());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

**Proceso:**
1. Abre un `ObjectInputStream` conectado al archivo
2. Lee el objeto con `readObject()`
3. Hace casting al tipo correcto `(Persona)`
4. Usa el objeto recuperado

## 🚀 Ejecución

### Paso 1: Compilar

```bash
javac Persona.java
javac EjemploPersonaSerializable.java
javac LeerPersona.java
```

### Paso 2: Serializar (Guardar)

```bash
java EjemploPersonaSerializable
```

**Salida esperada:**
```
Objeto Persona serializado y guardado en persona.ser
```

**Resultado:** Se crea el archivo `persona.ser` con los datos del objeto.

### Paso 3: Deserializar (Leer)

```bash
java LeerPersona
```

**Salida esperada:**
```
Nombre: Juan, Edad: 30
```

## 📊 Flujo de Datos

```
┌─────────────────┐
│  Objeto Persona │
│  nombre: "Juan" │
│  edad: 30       │
└────────┬────────┘
         │ Serialización
         │ (writeObject)
         ▼
┌─────────────────┐
│  persona.ser    │
│  [bytes binarios]│
└────────┬────────┘
         │ Deserialización
         │ (readObject)
         ▼
┌─────────────────┐
│  Objeto Persona │
│  nombre: "Juan" │
│  edad: 30       │
└─────────────────┘
```

## 🔧 Conceptos Técnicos Avanzados

### serialVersionUID

Para mejor control de versiones, es recomendable agregar un `serialVersionUID`:

```java
public class Persona implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nombre;
    private int edad;
    // ...
}
```

**¿Por qué es importante?**
- ✅ Control de compatibilidad entre versiones
- ✅ Evita `InvalidClassException` en cambios menores
- ✅ Permite evolución controlada de la clase

### Atributos Transient

Si NO quieres serializar ciertos campos:

```java
public class Persona implements Serializable {
    private String nombre;
    private int edad;
    private transient String password; // ❌ NO se serializa
    
    // ...
}
```

**Uso típico:**
- Contraseñas y datos sensibles
- Campos calculados o derivados
- Conexiones a bases de datos
- Streams de archivos

### Serialización Personalizada

Para control total sobre el proceso:

```java
public class Persona implements Serializable {
    private String nombre;
    private int edad;
    
    // Método llamado durante serialización
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject(); // Serializa campos normales
        // Lógica personalizada adicional
        out.writeUTF(nombre.toUpperCase()); // Ejemplo
    }
    
    // Método llamado durante deserialización
    private void readObject(ObjectInputStream in) 
            throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Deserializa campos normales
        // Lógica personalizada adicional
    }
}
```

## ⚠️ Excepciones Comunes

### 1. NotSerializableException

```
java.io.NotSerializableException: MiClase
```

**Causa:** La clase no implementa `Serializable`  
**Solución:** Añade `implements Serializable`

### 2. ClassNotFoundException

```
java.lang.ClassNotFoundException: Persona
```

**Causa:** La clase no está disponible al deserializar  
**Solución:** Asegúrate de que `Persona.class` existe en el classpath

### 3. InvalidClassException

```
java.io.InvalidClassException: Persona; local class incompatible
```

**Causa:** La versión de la clase cambió  
**Solución:** Define `serialVersionUID` explícitamente

### 4. FileNotFoundException

```
java.io.FileNotFoundException: persona.ser
```

**Causa:** El archivo no existe  
**Solución:** Ejecuta primero `EjemploPersonaSerializable`

## 🎓 Ejemplo Extendido

### Múltiples Objetos

```java
// Guardar múltiples personas
try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream("personas.ser"))) {
    oos.writeObject(new Persona("Juan", 30));
    oos.writeObject(new Persona("María", 25));
    oos.writeObject(new Persona("Pedro", 35));
    System.out.println("3 personas guardadas");
}

// Leer múltiples personas
try (ObjectInputStream ois = new ObjectInputStream(
        new FileInputStream("personas.ser"))) {
    Persona p1 = (Persona) ois.readObject();
    Persona p2 = (Persona) ois.readObject();
    Persona p3 = (Persona) ois.readObject();
    System.out.println(p1.getNombre() + ", " + 
                       p2.getNombre() + ", " + 
                       p3.getNombre());
}
```

### Colecciones de Objetos

```java
import java.util.ArrayList;
import java.util.List;

// Guardar una lista
List<Persona> personas = new ArrayList<>();
personas.add(new Persona("Juan", 30));
personas.add(new Persona("María", 25));

try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream("lista_personas.ser"))) {
    oos.writeObject(personas);
}

// Leer la lista
try (ObjectInputStream ois = new ObjectInputStream(
        new FileInputStream("lista_personas.ser"))) {
    @SuppressWarnings("unchecked")
    List<Persona> personasLeidas = (List<Persona>) ois.readObject();
    
    for (Persona p : personasLeidas) {
        System.out.println(p.getNombre() + ": " + p.getEdad());
    }
}
```

## 🔐 Seguridad

### ⚠️ Consideraciones de Seguridad

1. **No serialices datos sensibles** sin cifrado
2. **Valida los datos** al deserializar
3. **No confíes en archivos `.ser` de origen desconocido**
4. **Usa `transient` para campos sensibles**

### Ejemplo con Cifrado Básico

```java
// Nota: Para producción usa bibliotecas de cifrado robustas
import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

// Serializar con cifrado
SecretKey key = KeyGenerator.getInstance("AES").generateKey();
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, key);

SealedObject sealedPersona = new SealedObject(persona, cipher);
// Guardar sealedPersona en lugar de persona directamente
```

## 📚 Casos de Uso Reales

### 1. Caché de Sesión
```java
// Guardar sesión de usuario al cerrar
UserSession session = getCurrentSession();
serialize(session, "session.cache");

// Restaurar al reabrir
UserSession session = deserialize("session.cache");
```

### 2. Configuración de Aplicación
```java
// Guardar configuración
AppConfig config = new AppConfig();
config.setTheme("dark");
config.setLanguage("es");
serialize(config, "app.config");
```

### 3. Estado de Juego
```java
// Guardar partida
GameState state = new GameState();
state.setLevel(5);
state.setScore(1500);
serialize(state, "savegame.dat");
```

## 🎯 Mejores Prácticas

1. ✅ **Siempre define `serialVersionUID`** explícitamente
2. ✅ **Usa try-with-resources** para cerrar automáticamente
3. ✅ **Marca campos sensibles como `transient`**
4. ✅ **Documenta qué versión de la clase es compatible**
5. ✅ **Considera alternativas modernas** (JSON, XML, Protocol Buffers)
6. ✅ **Prueba la deserialización** con versiones antiguas
7. ✅ **No serialices objetos con estado externo** (conexiones BD, archivos)

## 🆚 Alternativas Modernas

| Método | Ventajas | Desventajas |
|--------|----------|-------------|
| **Serialización Java** | Nativa, fácil | Binario, solo Java, frágil |
| **JSON (Gson/Jackson)** | Legible, multiplataforma | Más grande, sin tipos |
| **XML (JAXB)** | Estándar, validable | Verboso, complejo |
| **Protocol Buffers** | Compacto, rápido | Requiere esquema |
| **MessagePack** | Muy compacto | Menos popular |

### Ejemplo con JSON (Gson)

```java
// Alternativa moderna más recomendada
import com.google.gson.Gson;

Gson gson = new Gson();

// Serializar a JSON
String json = gson.toJson(persona);
// Guardar json en archivo

// Deserializar desde JSON
Persona persona = gson.fromJson(json, Persona.class);
```

## 🐛 Debugging Tips

Si tienes problemas:

1. **Verifica que la clase implemente `Serializable`**
2. **Comprueba que todos los atributos sean serializables**
3. **Asegúrate de ejecutar primero el programa de escritura**
4. **Revisa que el archivo `.ser` se haya creado**
5. **Usa el mismo `serialVersionUID` en ambas versiones**

## 📝 Ejercicios Propuestos

1. 📝 Añade más atributos a `Persona` (dirección, teléfono)
2. 🔐 Marca el teléfono como `transient` y verifica que no se serialice
3. 📋 Crea una `List<Persona>` y serializa toda la lista
4. 🔢 Añade `serialVersionUID` y modifica la clase para ver compatibilidad
5. 🎯 Crea una clase `Empresa` con una lista de `Persona` (empleados)
6. 💾 Implementa un sistema de guardado automático cada 5 minutos
7. 🔄 Implementa `writeObject()` y `readObject()` personalizados

## 📖 Recursos Adicionales

- [Oracle Docs: Serializable](https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html)
- [Oracle Tutorial: Object Serialization](https://docs.oracle.com/javase/tutorial/jndi/objects/serial.html)
- [Effective Java - Item 85-90: Serialization](https://www.oreilly.com/library/view/effective-java/9780134686097/)

## 💡 Conclusión

La serialización de Java es una herramienta poderosa para persistir objetos, pero:
- ⚠️ Tiene limitaciones (solo Java, frágil ante cambios)
- ✅ Es útil para casos simples y rápidos
- 🎯 Para aplicaciones modernas, considera JSON o Protocol Buffers

**Regla de oro:** Usa serialización Java para prototipos y casos internos. Para APIs y persistencia a largo plazo, usa formatos textuales como JSON.

## 📄 Licencia

Proyecto educativo para aprendizaje de serialización de objetos en Java.

## 👤 Autor --> Judith Olmedo Andrés

Ejemplo práctico de serialización y deserialización de objetos en Java.
