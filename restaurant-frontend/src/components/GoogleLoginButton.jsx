import { useEffect, useRef } from "react";
import api from "../api/axios";
import { useNavigate } from "react-router-dom";

// should put it in .env
const GOOGLE_CLIENT_ID = import.meta.env.VITE_GOOGLE_CLIENT_ID;

export default function GoogleLoginButton() {
  const buttonRef = useRef(null);
  const navigate = useNavigate();

  const handleCredentialResponse = async (response) => {
    try {
      console.log("Google Response:", response);

      // response.credential is a JWT (Google ID token)
      const googleIdToken = response.credential;

      await api.post("/api/auth/google", {
        credential: googleIdToken,
      });

      console.log("Logged in via Google");
      navigate("/restaurant"); // redirect after login
    } catch (err) {
      console.error("Google login failed", err);
    }
  };

  useEffect(() => {
    if (!GOOGLE_CLIENT_ID) {
      console.error("VITE_GOOGLE_CLIENT_ID is not set.");
      return;
    }

    let intervalId = null;

    const tryInit = () => {
      if (window.google && buttonRef.current) {
        window.google.accounts.id.initialize({
          client_id: GOOGLE_CLIENT_ID,
          callback: handleCredentialResponse,
        });

        window.google.accounts.id.renderButton(buttonRef.current, {
          theme: "outline",
          size: "large",
          text: "continue_with",
          shape: "rectangular",
          width: 240,
        });

        return true;
      }
      return false;
    };

    if (!tryInit()) {
      intervalId = setInterval(() => {
        if (tryInit() && intervalId) {
          clearInterval(intervalId);
          intervalId = null;
        }
      }, 100);
    }

    return () => {
      if (intervalId) clearInterval(intervalId);
    };
  }, []);

  return <div ref={buttonRef}></div>;
}
