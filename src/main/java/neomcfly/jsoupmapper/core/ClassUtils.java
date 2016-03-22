package neomcfly.jsoupmapper.core;

public abstract class ClassUtils {

	
	@SuppressWarnings("unchecked")
	public static boolean isAssignable(Class class1, Class class2){
		
		try {
			return class1.isAssignableFrom(class2) || 
					class1.isPrimitive() && class1.equals(class2.getField("TYPE").get(null));
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			return false;
		}
		
	}
	
	
	
}
