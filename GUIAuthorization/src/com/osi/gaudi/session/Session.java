package com.osi.gaudi.session;

import com.osi.security.authentication.UserDTO;
import java.io.Serializable;
import java.util.Locale;
import java.util.UUID;

public final class Session
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private UserDTO userDTO;
  private String idSession;
  private String ip;
  private Locale locale;
  
  public Session(UserDTO userDTO, String ip, Locale locale)
  {
    this.userDTO = userDTO;
    this.idSession = UUID.randomUUID().toString();
    this.ip = ip;
    this.locale = locale;
  }
  
  public UserDTO getUserDTO()
  {
    return this.userDTO;
  }
  
  public String getIp()
  {
    return this.ip;
  }
  
  public String getIdSession()
  {
    return this.idSession;
  }
  
  public Locale getLocale()
  {
    return this.locale;
  }
}