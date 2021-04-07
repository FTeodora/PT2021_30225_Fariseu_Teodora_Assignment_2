package view;

public class BadInputException extends Exception{
    public BadInputException(String fieldName,String cause){
        super(fieldName+": "+cause);
    }
    public BadInputException(String cause){
        super(cause);
    }

}
