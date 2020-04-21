package com.example.runnablet;

import java.util.regex.Pattern;

public class LoginCheck {

    public boolean checkPass(String password){
        String regex = "^(.{2}[0-9].{2}[0-9][A-Z][!@#$%&*]){2}$";
        Pattern pattern = Pattern.compile(regex);
        if(pattern.matcher(password).matches()) return true;
        return false;
    }

    public boolean checkMail(String mail){
        String regex = "^(.{2}[0-9].{2}[0-9][A-Z][!@#$%&*]){2}$";
        Pattern pattern = Pattern.compile(regex);
        if(pattern.matcher(mail).matches()) return true;
        return false;

    }
}
