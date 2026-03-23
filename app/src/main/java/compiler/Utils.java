package compiler;

public  class Utils {

    public static boolean checkExists(Object[] array, Object target) 
    {
        for (Object array1 : array) {
            if (target.equals(array1)) {
                return true;
            }
        }
        return false;
    }
}
