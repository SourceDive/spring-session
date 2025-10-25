import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 简化的Session演示程序
 * 模拟Spring Session的核心概念，不依赖Spring Session源码
 */
public class SimpleSessionDemo {
    
    // 模拟Session接口
    public static class SimpleSession {
        private String id;
        private Map<String, Object> attributes = new HashMap<>();
        private long lastAccessedTime;
        private int maxInactiveInterval = 1800; // 30分钟
        
        public SimpleSession() {
            this.id = UUID.randomUUID().toString();
            this.lastAccessedTime = System.currentTimeMillis();
        }
        
        public String getId() {
            return id;
        }
        
        public void setAttribute(String name, Object value) {
            attributes.put(name, value);
            lastAccessedTime = System.currentTimeMillis();
        }
        
        public Object getAttribute(String name) {
            lastAccessedTime = System.currentTimeMillis();
            return attributes.get(name);
        }
        
        public void removeAttribute(String name) {
            attributes.remove(name);
            lastAccessedTime = System.currentTimeMillis();
        }
        
        public void setMaxInactiveInterval(int interval) {
            this.maxInactiveInterval = interval;
        }
        
        public boolean isExpired() {
            return (System.currentTimeMillis() - lastAccessedTime) > (maxInactiveInterval * 1000);
        }
        
        public Map<String, Object> getAttributes() {
            return new HashMap<>(attributes);
        }
    }
    
    // 模拟SessionRepository接口
    public static class SimpleSessionRepository {
        private Map<String, SimpleSession> sessions = new HashMap<>();
        
        public SimpleSession createSession() {
            SimpleSession session = new SimpleSession();
            sessions.put(session.getId(), session);
            return session;
        }
        
        public SimpleSession getSession(String sessionId) {
            SimpleSession session = sessions.get(sessionId);
            if (session != null && session.isExpired()) {
                sessions.remove(sessionId);
                return null;
            }
            return session;
        }
        
        public void save(SimpleSession session) {
            if (session.isExpired()) {
                sessions.remove(session.getId());
            } else {
                sessions.put(session.getId(), session);
            }
        }
        
        public void delete(String sessionId) {
            sessions.remove(sessionId);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== 简化版Session演示程序 ===");
        System.out.println("这个程序模拟了Spring Session的核心概念");
        
        // 创建SessionRepository
        SimpleSessionRepository repository = new SimpleSessionRepository();
        
        // 创建新Session
        SimpleSession session = repository.createSession();
        System.out.println("创建新Session，ID: " + session.getId());
        
        // 设置Session属性
        session.setAttribute("username", "debug_user");
        session.setAttribute("loginTime", System.currentTimeMillis());
        session.setAttribute("userRole", "admin");
        
        System.out.println("设置Session属性:");
        System.out.println("  username: " + session.getAttribute("username"));
        System.out.println("  loginTime: " + session.getAttribute("loginTime"));
        System.out.println("  userRole: " + session.getAttribute("userRole"));
        
        // 保存Session
        repository.save(session);
        System.out.println("Session已保存");
        
        // 读取Session
        SimpleSession retrievedSession = repository.getSession(session.getId());
        if (retrievedSession != null) {
            System.out.println("成功读取Session，ID: " + retrievedSession.getId());
            System.out.println("Session属性:");
            for (Map.Entry<String, Object> entry : retrievedSession.getAttributes().entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
        }
        
        // 演示Session过期
        System.out.println("\n=== Session过期演示 ===");
        session.setMaxInactiveInterval(1); // 1秒后过期
        repository.save(session);
        
        try {
            Thread.sleep(2000); // 等待2秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        SimpleSession expiredSession = repository.getSession(session.getId());
        if (expiredSession == null) {
            System.out.println("Session已过期，无法读取");
        }
        
        // 演示属性操作
        System.out.println("\n=== 属性操作演示 ===");
        SimpleSession newSession = repository.createSession();
        newSession.setAttribute("counter", 1);
        System.out.println("初始计数器: " + newSession.getAttribute("counter"));
        
        // 模拟计数器递增
        int counter = (Integer) newSession.getAttribute("counter");
        newSession.setAttribute("counter", counter + 1);
        System.out.println("递增后计数器: " + newSession.getAttribute("counter"));
        
        // 删除属性
        newSession.removeAttribute("counter");
        System.out.println("删除后计数器: " + newSession.getAttribute("counter"));
        
        System.out.println("\n=== 演示完成 ===");
        System.out.println("这个演示展示了Session的核心概念：");
        System.out.println("1. Session ID的生成和管理");
        System.out.println("2. Session属性的存储和访问");
        System.out.println("3. Session过期机制");
        System.out.println("4. Session的创建、保存、读取和删除");
        System.out.println("\n这些概念在Spring Session中都有对应的实现，");
        System.out.println("你可以通过阅读Spring Session源码来了解更复杂的实现。");
    }
}

