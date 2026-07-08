document.addEventListener("DOMContentLoaded", () => {
    const summaryForm = document.getElementById("summaryForm");
    const fileInput = document.getElementById("file");
    const resultBox = document.getElementById("result");

    summaryForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const file = fileInput.files[0];

        if (!file) {
            showMessage(resultBox, "PDF 파일을 선택해주세요.");
            return;
        }

        if (file.type !== "application/pdf") {
            showMessage(resultBox, "PDF 파일만 업로드할 수 있습니다.");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);
        formData.append("conversationId", "lecture-summary");

        showLoading(resultBox);

        try {
            const response = await fetch("/api/lecture-summary", {
                method: "POST",
                body: formData
            });

            if (!response.ok) {
                const errorText = await response.text();
                showMessage(resultBox, `요청 실패\n상태 코드: ${response.status}\n${errorText}`);
                return;
            }

            const data = await response.json();
            renderSummary(resultBox, data);

        } catch (error) {
            showMessage(resultBox, "요청 중 오류가 발생했습니다.\n" + error.message);
        }
    });
});

function showLoading(target) {
    target.innerHTML = `
        <div class="loading-box">
            <div class="spinner"></div>
            <p>강의자료를 분석하는 중입니다...</p>
        </div>
    `;
}

function showMessage(target, message) {
    target.innerHTML = `
        <p class="empty-message">${escapeHtml(message)}</p>
    `;
}

function renderSummary(target, data) {
    target.innerHTML = `
        <section class="summary-section">
            <h3>📝 강의 전체 요약</h3>
            <p>${escapeHtml(data.summary || "요약 내용이 없습니다.")}</p>
        </section>

        <section class="summary-section">
            <h3>📌 핵심 개념</h3>
            ${renderList(data.concepts)}
        </section>

        <section class="summary-section">
            <h3>🔑 핵심 키워드</h3>
            <div class="keyword-list">
                ${renderKeywords(data.keywords)}
            </div>
        </section>

        <section class="summary-section">
            <h3>❓ 예상 질문</h3>
            ${renderList(data.questions)}
        </section>
    `;
}

function renderList(items) {
    if (!items || items.length === 0) {
        return `<p class="empty-message">내용이 없습니다.</p>`;
    }

    return `
        <ol>
            ${items.map(item => `<li>${escapeHtml(item)}</li>`).join("")}
        </ol>
    `;
}

function renderKeywords(items) {
    if (!items || items.length === 0) {
        return `<span class="keyword">키워드 없음</span>`;
    }

    return items
        .map(item => `<span class="keyword">${escapeHtml(item)}</span>`)
        .join("");
}

function escapeHtml(text) {
    return String(text)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}