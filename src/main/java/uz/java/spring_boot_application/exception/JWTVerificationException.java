package uz.java.spring_boot_application.exception;

public class JWTVerificationException extends RuntimeException{
    public JWTVerificationException(String message){
        super(message);
    }
}
