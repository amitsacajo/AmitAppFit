package com.example.amitappfit.services;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import org.jetbrains.annotations.NotNull;

/**
 * שירות לאינטראקציה עם Firebase Authentication.
 * מחלקה זו ממומשת כ-singleton, יש להשתמש ב-getInstance() כדי לקבל מופע שלה.
 * @see FirebaseAuth
 */
public class AuthenticationService {

    /**
     * תג ללוגים.
     * @see Log
     */
    private static final String TAG = "AuthenticationService";

    /**
     * ממשק callback עבור פעולות אימות.
     * יש לממש את הממשק כדי לקבל התראה על הצלחה או כישלון בפעולות האימות.
     * @see AuthCallback#onCompleted(String uid)
     * @see AuthCallback#onFailed(Exception)
     */
    public interface AuthCallback {
        /**
         * נקרא כאשר הפעולה הושלמה בהצלחה.
         * @param uid מזהה המשתמש שהצליח להתחבר/להירשם
         */
        void onCompleted(String uid);

        /**
         * נקרא כאשר הפעולה נכשלה עם חריגה.
         * @param e החריגה שהסבירה את הכישלון
         */
        void onFailed(Exception e);
    }

    /**
     * מופע סינגלטון של המחלקה.
     * @see #getInstance()
     */
    private static AuthenticationService instance;

    /**
     * מופע Firebase Authentication.
     * @see FirebaseAuth
     */
    private final FirebaseAuth mAuth;

    /**
     * קונסטרקטור פרטי, למניעת יצירת מופעים ישירות.
     * יש להשתמש ב-getInstance() לקבלת מופע יחיד.
     */
    private AuthenticationService() {
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * מחזיר את מופע הסינגלטון של המחלקה.
     * במידה ולא קיים מופע, יווצר מופע חדש.
     * @return מופע של AuthenticationService
     */
    public static AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    /**
     * מבצע התחברות של משתמש באמצעות מייל וסיסמה.
     * @param email המייל של המשתמש
     * @param password הסיסמה של המשתמש
     * @param callback callback שיקבל את התוצאה - הצלחה או כישלון
     *                 בהצלחה יקבל את מזהה המשתמש (UID)
     *                 בכישלון יקבל את החריגה שקרתה
     */
    public void signIn(@NotNull final String email, @NotNull final String password, @NotNull final AuthCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onCompleted(getCurrentUserId());
            } else {
                Log.e(TAG, "שגיאה בהתחברות", task.getException());
                callback.onFailed(task.getException());
            }
        });
    }

    /**
     * מבצע רישום משתמש חדש באמצעות מייל וסיסמה.
     * @param email המייל של המשתמש החדש
     * @param password הסיסמה של המשתמש החדש
     * @param callback callback שיקבל את התוצאה - הצלחה או כישלון
     *                 בהצלחה יקבל את מזהה המשתמש (UID)
     *                 בכישלון יקבל את החריגה שקרתה
     */
    public void signUp(@NotNull final String email, @NotNull final String password, @NotNull final AuthCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onCompleted(getCurrentUserId());
            } else {
                Log.e(TAG, "שגיאה בהרשמה", task.getException());
                callback.onFailed(task.getException());
            }
        });
    }

    /**
     * מבצע יציאה (Sign Out) של המשתמש הנוכחי.
     */
    public void signOut() {
        mAuth.signOut();
    }

    /**
     * מחזיר את מזהה המשתמש הנוכחי (UID).
     * @return מזהה המשתמש או null אם אין משתמש מחובר
     */
    public String getCurrentUserId() {
        if (mAuth.getCurrentUser() == null) {
            return null;
        }
        return mAuth.getCurrentUser().getUid();
    }

    /**
     * בודק אם יש משתמש מחובר כרגע.
     * @return true אם משתמש מחובר, false אחרת
     */
    public boolean isUserSignedIn() {
        return mAuth.getCurrentUser() != null;
    }
}
