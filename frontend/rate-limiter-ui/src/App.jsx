import { useState, useEffect } from "react";

export default function App() {
  const [apiKey, setApiKey] = useState("user1");
  const [data, setData] = useState(null);
  const [status, setStatus] = useState("");
  const [loading, setLoading] = useState(false);

  const callApi = async () => {
    setLoading(true);
    setStatus("");

    try {
      const res = await fetch(
        "https://api-rate-limiter-xknt.onrender.com/api/test",
        {
          method: "GET",
          headers: {
            "X-API-KEY": apiKey,
          },
        }
      );

      if (res.status === 429) {
        setStatus("RATE_LIMITED");
        setData(null);
        setLoading(false);
        return;
      }

      const json = await res.json();
      setData(json);
      setStatus("SUCCESS");
    } catch (err) {
      setStatus("ERROR");
      setData(null);
    }

    setLoading(false);
  };

  // 🔥 Auto refresh tokens every 2 sec
  useEffect(() => {
    if (!data) return;

    const interval = setInterval(async () => {
      try {
        const res = await fetch(
          "https://api-rate-limiter-xknt.onrender.com/api/test",
          {
            method: "GET",
            headers: {
              "X-API-KEY": apiKey,
            },
          }
        );

        if (res.status === 200) {
          const json = await res.json();
          setData(json);
        }
      } catch {}
    }, 2000);

    return () => clearInterval(interval);
  }, [apiKey, data]);

  const percentage = data
    ? (data.requestsLeft / data.limit) * 100
    : 0;

  return (
    <div style={styles.container}>
      <h2 style={styles.title}>API Rate Limiter</h2>

      <div style={styles.card}>
        <input
          value={apiKey}
          onChange={(e) => setApiKey(e.target.value)}
          style={styles.input}
        />

        <button onClick={callApi} style={styles.button}>
          {loading ? "Sending..." : "Send Request"}
        </button>
      </div>

      {status && (
        <div
          style={{
            ...styles.status,
            color:
              status === "SUCCESS"
                ? "#16a34a"
                : status === "RATE_LIMITED"
                ? "#dc2626"
                : "#d97706",
          }}
        >
          {status === "SUCCESS" && "Request Successful"}
          {status === "RATE_LIMITED" && "Rate Limit Exceeded"}
          {status === "ERROR" && "Connection Error"}
        </div>
      )}

      {data && (
        <div style={styles.card}>
          <p><strong>Plan:</strong> {data.plan}</p>
          <p><strong>Limit:</strong> {data.limit} requests</p>
          <p><strong>Remaining:</strong> {data.requestsLeft}</p>

          <div style={styles.progressContainer}>
            <div
              style={{
                ...styles.progressBar,
                width: `${percentage}%`,
              }}
            />
          </div>

          <p style={{ fontSize: "12px", color: "#64748b", marginTop: "8px" }}>
            Tokens auto-refill over time
          </p>
        </div>
      )}
    </div>
  );
}

const styles = {
  container: {
    fontFamily: "Arial",
    padding: "40px",
    textAlign: "center",
    background: "linear-gradient(to right, #eef2ff, #f8fafc)",
    minHeight: "100vh",
  },
  title: {
    marginBottom: "25px",
    color: "#1e3a8a", // deep blue
    fontWeight: "600",
    letterSpacing: "0.5px",
  },
  card: {
    background: "#ffffff",
    padding: "20px",
    margin: "15px auto",
    borderRadius: "10px",
    width: "320px",
    boxShadow: "0 4px 10px rgba(0,0,0,0.05)",
  },
  input: {
    padding: "10px",
    width: "100%",
    marginBottom: "10px",
    borderRadius: "6px",
    border: "1px solid #cbd5e1",
  },
  button: {
    padding: "10px",
    width: "100%",
    borderRadius: "6px",
    border: "none",
    background: "#2563eb",
    color: "white",
    cursor: "pointer",
    fontWeight: "500",
  },
  status: {
    marginTop: "10px",
    fontWeight: "600",
  },
  progressContainer: {
    background: "#e2e8f0",
    borderRadius: "6px",
    height: "8px",
    marginTop: "10px",
  },
  progressBar: {
    height: "8px",
    background: "#2563eb",
    borderRadius: "6px",
  },
};