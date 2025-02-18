package modelo.Descuento;

import java.lang.reflect.Constructor;

public class FactoriaDescuento {
    

    public static Descuento crearDescuento(String claseNombre, Object... parametros) {
        try {
            Class<?> clase = Class.forName(claseNombre);

            if (!Descuento.class.isAssignableFrom(clase)) {
                throw new IllegalArgumentException("La clase " + claseNombre + " no es un tipo válido de Descuento");
            }

            Class<?>[] tiposParametros = new Class<?>[parametros.length];
            for (int i = 0; i < parametros.length; i++) {
                if (parametros[i] instanceof Double) {
                    tiposParametros[i] = double.class;  
                } 
                else if (parametros[i] instanceof Integer) {
                    tiposParametros[i] = int.class;  
                } else {
                    tiposParametros[i] = parametros[i].getClass();
                }
            }

            Constructor<?> constructor = clase.getConstructor(tiposParametros);


            return (Descuento) constructor.newInstance(parametros);

        } catch (Exception e) {
            throw new RuntimeException("Error al crear el descuento: " + e.getMessage(), e);
        }
    }
}
