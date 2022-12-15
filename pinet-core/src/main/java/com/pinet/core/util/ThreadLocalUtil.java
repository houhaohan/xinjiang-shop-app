package com.pinet.core.util;

/**
 * @author chengshuanghui on 2022/12/12
 */
public class ThreadLocalUtil {

    private static ThreadLocal<UserLogin> USER_LOGIN = new ThreadLocal<UserLogin>();

    public static UserLogin getUserLogin() {
        UserLogin userLogin = USER_LOGIN.get();
        return userLogin != null ? userLogin : new UserLogin(0L);
    }

    public static void setUserId(Long userId) {
        if (USER_LOGIN.get() != null) {
            USER_LOGIN.get().setUserId(userId);
        } else {
            UserLogin userLogin = new UserLogin(userId);
            USER_LOGIN.set(userLogin);
        }
    }

    public static void clear() {
        USER_LOGIN.set(null);
    }

    public static void remove() {
        USER_LOGIN.remove();
    }

    public static class UserLogin {

        private Long userId;

        public UserLogin(Long userId) {
        	this.userId = userId;
        }

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}
	}
}
