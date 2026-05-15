package compiler;
public class CompilerRunner {

    public static class ByteClassLoader extends ClassLoader {
        public Class<?> defineClass(String name, byte[] bytecode) {
            return defineClass(name, bytecode, 0, bytecode.length);
        }
    }

    public static void runInMemory(String className, byte[] generatedByteCode) throws Exception {
        ByteClassLoader loader = new ByteClassLoader();
        Class<?> compiledClass = loader.defineClass(className, generatedByteCode);

        java.lang.reflect.Method mainMethod = compiledClass.getMethod("main", String[].class);

        String[] params = new String[0];
        mainMethod.invoke(null, (Object) params);
    }
}