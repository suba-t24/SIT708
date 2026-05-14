from flask import Flask, request, jsonify
from flask_cors import CORS
import requests

app = Flask(__name__)
CORS(app)

# Set to True to use Ollama phi3:mini
# Set to False to use lightweight fallback replies
USE_OLLAMA = True

OLLAMA_URL = "http://localhost:11434/api/generate"
OLLAMA_MODEL = "phi3:mini"


def fallback_reply(message):
    message_lower = message.lower()

    if "hello" in message_lower or "hi" in message_lower:
        return "Hello! I am your AI chatbot. How can I help you today?"

    if "name" in message_lower:
        return "I am an LLM-powered chatbot built for the SIT708 Task 8.1."

    if "help" in message_lower:
        return "I can answer questions, explain ideas, and assist with simple learning tasks."

    if "android" in message_lower:
        return "Android development uses activities, XML layouts, intents, APIs, and local storage such as SQLite or Room."

    if "sqlite" in message_lower or "room" in message_lower:
        return "SQLite is a lightweight local database, and Room is an Android library that makes SQLite easier to use for storing chat history."

    if "ai" in message_lower or "artificial intelligence" in message_lower:
        return "Artificial Intelligence allows computers to perform tasks that usually need human intelligence, such as answering questions, recognising patterns, and making decisions."

    if "study tips" in message_lower:
        return "Here are three study tips: plan your tasks early, revise regularly, and take short breaks to stay focused."

    return "That is an interesting question. This chatbot backend can respond using either Ollama phi3:mini or a lightweight fallback response."


def ollama_reply(message):
    payload = {
        "model": OLLAMA_MODEL,
        "prompt": message,
        "stream": False,
        "options": {
            "num_predict": 120
        }
    }

    response = requests.post(OLLAMA_URL, json=payload, timeout=90)

    if response.status_code == 200:
        data = response.json()
        return data.get("response", "No response generated.").strip()

    return fallback_reply(message)


@app.route("/", methods=["GET"])
def home():
    return jsonify({
        "status": "running",
        "message": "LLM ChatBot backend is running",
        "ollama_enabled": USE_OLLAMA,
        "model": OLLAMA_MODEL if USE_OLLAMA else "fallback"
    })


@app.route("/chat", methods=["POST"])
def chat():
    data = request.get_json()

    username = data.get("username", "User")
    message = data.get("message", "").strip()

    if not message:
        return jsonify({
            "username": username,
            "reply": "Please enter a message."
        })

    try:
        if USE_OLLAMA:
            reply = ollama_reply(message)
        else:
            reply = fallback_reply(message)

    except Exception:
        reply = fallback_reply(message)

    return jsonify({
        "username": username,
        "reply": reply
    })


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001, debug=True)
