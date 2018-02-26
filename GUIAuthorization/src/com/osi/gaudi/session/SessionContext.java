package com.osi.gaudi.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.osi.gaudi.exception.Failure;
import com.osi.gaudi.exception.FailureMessage;
import com.osi.gaudi.exception.Messenger;

public class SessionContext
{
  private final Logger log = LoggerFactory.getLogger(SessionContext.class);
  private static ThreadLocal<Session> tLocal = new ThreadLocal<Session>();
  private static SessionContext instance;
  
  public static SessionContext getInstance()
  {
    if (instance == null) {
      instance = new SessionContext();
    }
    return instance;
  }
  
  public void setSession(Session session)
  {
    tLocal.set(session);
  }
  
  public Session getSession()
  {
    return getSession(true);
  }
  
  public Session getSession(boolean failNotSession)
  {
    Session session = (Session)tLocal.get();
    if ((session == null) && (failNotSession))
    {
      FailureMessage msg = Messenger.getInstance().getFailureMessage(
        "gaudi", "error.sessionNotFound", new Object[0]);
      this.log.error(msg.getMessage());
      throw new Failure(msg.getCode(), msg.getMessage(), msg.getFixes());
    }
    return session;
  }
  
  public void cleanUpSession()
  {
    tLocal.remove();
  }
  
  public String getDataFormatted()
  {
    StringBuilder sb = new StringBuilder();
    Session sessionData = getSession();
    if (sessionData != null)
    {
      sb.append("Id session= " + sessionData.getIdSession() + "\n");
      sb.append("Ip= " + sessionData.getIp() + "\n");
      sb.append("Locale= " + sessionData.getLocale() + "\n");
      if (sessionData.getUserDTO() != null) {
        sb.append("UserName= " + sessionData.getUserDTO().getUserName() + 
          "\n");
      }
    }
    return sb.toString();
  }
}