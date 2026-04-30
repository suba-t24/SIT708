from flask import Flask, request, jsonify
from flask_cors import CORS
import requests

app = Flask(__name__)
CORS(app)

OLLAMA_URL = "http://localhost:11434/api/generate"
MODEL_NAME = "phi3:mini"

# Set False for smooth demo. Set True only when you want one real AI response.
USE_OLLAMA = True


def fallback_response(prompt, utility_type):
    if utility_type == "hint":
        return (
            "Hint: Focus on the key concept in the question. Try eliminating options "
            "that do not match the definition or behaviour being asked."
        )

    if utility_type == "explain_answer":
        return (
            "Explanation: The correct answer matches the main concept tested in the question. "
            "Compare the selected answer with the correct answer and identify why the other "
            "options are less suitable."
        )

    if utility_type == "summary":
        return (
            "Summary: This lesson explains the core idea of the topic, how it is applied in "
            "practice, and why it is useful for solving programming and software problems."
        )

    if utility_type == "flashcards":
        return (
            "Flashcard 1\n"
            "Q: What is the main idea of this topic?\n"
            "A: It explains a key concept used in software development.\n\n"
            "Flashcard 2\n"
            "Q: Why is this topic useful?\n"
            "A: It helps students solve problems more clearly and efficiently.\n\n"
            "Flashcard 3\n"
            "Q: How can this topic be revised?\n"
            "A: Review the definition, practise examples, and test yourself with quiz questions."
        )

    if utility_type == "study_plan":
        return (
            "7-Day Study Plan:\n"
            "Day 1: Review the basic concepts.\n"
            "Day 2: Practise simple examples.\n"
            "Day 3: Complete quiz questions.\n"
            "Day 4: Review incorrect answers.\n"
            "Day 5: Create flashcards.\n"
            "Day 6: Practise mixed questions.\n"
            "Day 7: Revise weak areas and summarise your learning."
        )

    return "Generated learning response based on the given prompt."


def call_ollama(prompt):
    if not USE_OLLAMA:
        print("OLLAMA DISABLED: using fallback response")
        return None

    try:
        payload = {
            "model": MODEL_NAME,
            "prompt": prompt,
            "stream": False,
            "options": {
                "num_predict": 120,
                "temperature": 0.4
            }
        }

        response = requests.post(OLLAMA_URL, json=payload, timeout=90)

        print("OLLAMA STATUS:", response.status_code)
        print("OLLAMA BODY:", response.text[:500])

        if response.status_code == 200:
            return response.json().get("response", "").strip()

        return None

    except Exception as e:
        print("OLLAMA ERROR:", str(e))
        return None


@app.route("/", methods=["GET"])
def home():
    return jsonify({
        "message": "LLM Learning Assistant backend is running",
        "ollama_enabled": USE_OLLAMA,
        "model": MODEL_NAME
    })


@app.route("/generate", methods=["POST"])
def generate():
    data = request.get_json()

    if not data:
        return jsonify({
            "response": "",
            "source": "error",
            "error": "No JSON body received"
        }), 400

    prompt = data.get("prompt", "").strip()
    utility_type = data.get("utilityType", "").strip()

    if not prompt:
        return jsonify({
            "response": "",
            "source": "error",
            "error": "Prompt cannot be empty"
        }), 400

    llm_response = call_ollama(prompt)

    if llm_response:
        return jsonify({
            "response": llm_response,
            "source": "ollama"
        })

    return jsonify({
        "response": fallback_response(prompt, utility_type),
        "source": "fallback"
    })


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001, debug=True)
