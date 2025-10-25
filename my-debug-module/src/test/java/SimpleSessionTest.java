import org.junit.Test;
import org.springframework.session.Session;
import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;
import org.springframework.session.MapSessionRepository;

import static org.junit.Assert.*;

/**
 * 简单的Spring Session测试类
 * 用于验证Spring Session的基本功能
 */
public class SimpleSessionTest {
    
    @Test
    public void testCreateAndRetrieveSession() {
        // 创建SessionRepository
        SessionRepository repository = new MapSessionRepository();
        
        // 创建新Session
        Session session = repository.createSession();
        assertNotNull("Session不应为null", session);
        assertNotNull("Session ID不应为null", session.getId());
        
        // 设置属性
        session.setAttribute("testKey", "testValue");
        assertEquals("属性值应该正确", "testValue", session.getAttribute("testKey"));
        
        // 保存Session
        repository.save(session);
        
        // 读取Session
        Session retrievedSession = repository.getSession(session.getId());
        assertNotNull("读取的Session不应为null", retrievedSession);
        assertEquals("Session ID应该相同", session.getId(), retrievedSession.getId());
        assertEquals("属性值应该相同", "testValue", retrievedSession.getAttribute("testKey"));
    }
    
    @Test
    public void testSessionExpiration() {
        SessionRepository repository = new MapSessionRepository();
        ExpiringSession session = (ExpiringSession) repository.createSession();
        
        // 设置很短的过期时间
        session.setMaxInactiveIntervalInSeconds(1);
        repository.save(session);
        
        // 立即读取应该成功
        Session retrievedSession = repository.getSession(session.getId());
        assertNotNull("立即读取应该成功", retrievedSession);
        
        // 等待过期
        try {
            Thread.sleep(1100); // 等待1.1秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 过期后读取应该失败
        Session expiredSession = repository.getSession(session.getId());
        assertNull("过期后应该无法读取", expiredSession);
    }
    
    @Test
    public void testSessionAttributes() {
        SessionRepository repository = new MapSessionRepository();
        Session session = repository.createSession();
        
        // 测试设置和获取属性
        session.setAttribute("stringAttr", "hello");
        session.setAttribute("intAttr", 123);
        session.setAttribute("objectAttr", new Object());
        
        assertEquals("字符串属性", "hello", session.getAttribute("stringAttr"));
        assertEquals("整数属性", (Integer) 123, session.getAttribute("intAttr"));
        assertNotNull("对象属性", session.getAttribute("objectAttr"));
        
        // 测试删除属性
        session.removeAttribute("stringAttr");
        assertNull("删除后应该为null", session.getAttribute("stringAttr"));
    }
}

