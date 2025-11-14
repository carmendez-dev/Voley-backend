package com.voley.dto;

/**
 * DTO para cambiar contraseña de profesor
 */
public class CambiarPasswordDTO {
    
    private String password;
    
    // Constructores
    public CambiarPasswordDTO() {}
    
    public CambiarPasswordDTO(String password) {
        this.password = password;
    }
    
    // Getters y Setters
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
