document.addEventListener("DOMContentLoaded", () => {

    const questionInput = document.getElementById("question");
    const sendBtn = document.getElementById("sendBtn");
    const newChatBtn = document.getElementById("newChatBtn");
    const chatHistory = document.getElementById("chatHistory");

    let chatCount = 1;
    let chatId = `chat-${chatCount}`;

    sendBtn.addEventListener("click", sendQuestion);

    newChatBtn.addEventListener("click", () => {

        chatCount++;
        chatId = `chat-${chatCount}`;

        chatHistory.innerHTML = `
        <div class="message ai">
            <div class="profile">🤖</div>
            <div class="bubble">
                새로운 대화를 시작했습니다.
                여행지를 입력해주세요.
            </div>
        </div>
        `;

        questionInput.value = "";
        questionInput.focus();
    });

    questionInput.addEventListener("keydown", e => {

        if (e.ctrlKey && e.key === "Enter") {
            sendQuestion();
        }

    });

    async function sendQuestion() {

        const question = questionInput.value.trim();

        if (!question) {
            alert("여행 요청을 입력하세요.");
            return;
        }

        appendUser(question);

        questionInput.value = "";

        sendBtn.disabled = true;
        sendBtn.textContent = "생성 중...";

        try {

            const params = new URLSearchParams({
                chatId,
                question
            });

            const response = await fetch(`/api/travel/chat?${params}`);

            const answer = await response.text();

            appendAI(answer);

        } catch (e) {

            appendAI("오류가 발생했습니다.\n" + e.message);

        } finally {

            sendBtn.disabled = false;
            sendBtn.textContent = "여행 일정 생성";

        }

    }

    function appendUser(text) {

        chatHistory.innerHTML += `
        <div class="message user">
            <div class="bubble">${text}</div>
            <div class="profile">👤</div>
        </div>
        `;

        scrollBottom();

    }

    function appendAI(text) {

        chatHistory.innerHTML += `
        <div class="message ai">
            <div class="profile">🤖</div>
            <div class="bubble">${text}</div>
        </div>
        `;

        scrollBottom();

    }

    function scrollBottom() {
        chatHistory.scrollTop = chatHistory.scrollHeight;
    }

});