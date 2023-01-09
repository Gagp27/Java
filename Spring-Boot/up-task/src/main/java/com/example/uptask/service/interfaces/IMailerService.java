package com.example.uptask.service.interfaces;


public interface IMailerService {

    public void sendRegisterEmail(Long userId, String userName, String email, String token);

    public void sendRecoverEmail(Long userId, String userName, String email, String token);
}
