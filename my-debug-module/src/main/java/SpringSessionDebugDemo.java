import org.springframework.session.Session;
import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;
import org.springframework.session.MapSessionRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring Session 源码阅读调试演示类
 * 
 * 这个类展示了Spring Session的核心概念和基本用法，
 * 作为阅读源码的入口点。
 */
public class SpringSessionDebugDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Spring Session 源码阅读调试演示 ===");
        
        // 1. 创建SessionRepository
        SessionRepository sessionRepository = new MapSessionRepository();
        
        // 2. 创建一个新的Session
        ExpiringSession session = (ExpiringSession) sessionRepository.createSession();
        System.out.println("创建新Session，ID: " + session.getId());
        
        // 3. 设置Session属性
        session.setAttribute("username", "debug_user");
        session.setAttribute("loginTime", System.currentTimeMillis());
        System.out.println("设置Session属性: username=debug_user, loginTime=" + session.getAttribute("loginTime"));
        
        // 4. 保存Session
        sessionRepository.save(session);
        System.out.println("Session已保存");
        
        // 5. 读取Session
        Session retrievedSession = sessionRepository.getSession(session.getId());
        if (retrievedSession != null) {
            System.out.println("成功读取Session，ID: " + retrievedSession.getId());
            System.out.println("Session属性 - username: " + retrievedSession.getAttribute("username"));
            System.out.println("Session属性 - loginTime: " + retrievedSession.getAttribute("loginTime"));
        }
        
        // 6. 演示Session过期
        System.out.println("\n=== Session过期演示 ===");
        session.setMaxInactiveIntervalInSeconds(1); // 1秒后过期
        sessionRepository.save(session);
        
        try {
            Thread.sleep(2000); // 等待2秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Session expiredSession = sessionRepository.getSession(session.getId());
        if (expiredSession == null) {
            System.out.println("Session已过期，无法读取");
        }
        
        System.out.println("\n=== 演示完成 ===");
        System.out.println("这个演示展示了Spring Session的核心功能：");
        System.out.println("1. SessionRepository接口 - 管理Session的存储");
        System.out.println("2. Session接口 - 表示一个HTTP会话");
        System.out.println("3. MapSessionRepository - 基于内存的Session存储实现");
        System.out.println("4. Session的创建、保存、读取和过期机制");
    }
}

