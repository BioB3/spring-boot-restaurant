import React, { useState } from "react";
import api from "../api/axios";
import { useNavigate } from "react-router-dom";

export default function AddRestaurant() {
  const navigate = useNavigate?.() ?? null;
  const [name, setName] = useState("");
  const [rating, setRating] = useState("");
  const [location, setLocation] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const resetMessages = () => {
    setError("");
    setSuccess("");
  };

  const validate = () => {
    if (!name.trim()) {
      setError("Name is required.");
      return false;
    }
    if (!location.trim()) {
      setError("Location is required.");
      return false;
    }

    const r = parseFloat(rating);
    if (Number.isNaN(r)) {
      setError("Rating must be a number.");
      return false;
    }
    if (r < 0 || r > 5) {
      setError("Rating must be between 0 and 5.");
      return false;
    }
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    resetMessages();

    if (!validate()) return;

    const payload = {
      name: name.trim(),
      rating: parseFloat(rating),
      location: location.trim(),
    };

    setLoading(true);
    try {
      await api.post("/api/restaurants", payload);

      setSuccess("Restaurant added successfully.");
      setName("");
      setRating("");
      setLocation("");

      if (navigate) {
        setTimeout(() => navigate("/restaurant"), 600);
      }
    } catch (err) {
      if (err.response) {
        const data = err.response.data;
        const message =
          (data && (data.message || JSON.stringify(data))) ||
          err.response.statusText ||
          `Status ${err.response.status}`;
        setError(`Server responded ${err.response.status}: ${message}`);
      } else if (err.request) {
        setError("No response from server (network or CORS issue).");
      } else {
        setError(err.message || "Failed to add restaurant");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 560, margin: "1rem auto", padding: "1rem" }}>
      <h2>Add New Restaurant</h2>

      <form onSubmit={handleSubmit} noValidate>
        <div style={{ marginBottom: 12 }}>
          <label style={{ display: "block", marginBottom: 6 }}>
            Name
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
              style={{ width: "100%", padding: "8px", marginTop: 4 }}
              placeholder="Restaurant name"
            />
          </label>
        </div>

        <div style={{ marginBottom: 12 }}>
          <label style={{ display: "block", marginBottom: 6 }}>
            Rating (0.0 - 5.0)
            <input
              type="number"
              value={rating}
              onChange={(e) => setRating(e.target.value)}
              required
              step="0.1"
              min="0"
              max="5"
              style={{ width: "100%", padding: "8px", marginTop: 4 }}
              placeholder="e.g. 4.5"
            />
          </label>
        </div>

        <div style={{ marginBottom: 12 }}>
          <label style={{ display: "block", marginBottom: 6 }}>
            Location
            <input
              type="text"
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              required
              style={{ width: "100%", padding: "8px", marginTop: 4 }}
              placeholder="Address or description"
            />
          </label>
        </div>

        {error && (
          <div style={{ color: "crimson", marginBottom: 12 }}>{error}</div>
        )}
        {success && (
          <div style={{ color: "green", marginBottom: 12 }}>{success}</div>
        )}

        <button
          type="submit"
          disabled={loading}
          style={{
            padding: "10px 16px",
            background: "#1976d2",
            color: "white",
            border: "none",
            cursor: loading ? "not-allowed" : "pointer",
          }}
        >
          {loading ? "Saving..." : "Add Restaurant"}
        </button>
      </form>
    </div>
  );
}
