document.addEventListener("DOMContentLoaded", () => {

    const recommendBtn = document.getElementById("recommendBtn");

    recommendBtn.addEventListener("click", recommendMenu);

});

async function recommendMenu() {

    const mood = document.getElementById("mood").value.trim();
    const weather = document.getElementById("weather").value;
    const budget = document.getElementById("budget").value;
    const spicy = document.getElementById("spicy").value;
    const alone = document.getElementById("alone").value;

    if (!mood) {
        alert("오늘 기분을 입력해주세요.");
        return;
    }

    try {

        const response = await fetch(
            `/api/menu?mood=${encodeURIComponent(mood)}`
            + `&weather=${encodeURIComponent(weather)}`
            + `&budget=${encodeURIComponent(budget)}`
            + `&spicy=${encodeURIComponent(spicy)}`
            + `&alone=${encodeURIComponent(alone)}`
        );

        if (!response.ok) {
            throw new Error("서버 요청 실패");
        }

        const result = await response.json();

        console.log("AI 응답 :", result);

        document.getElementById("menu").textContent = result.menu;
        document.getElementById("category").textContent = result.category;
        document.getElementById("priceRange").textContent = result.priceRange;
        document.getElementById("reason").textContent = result.reason;

        const confidence = document.getElementById("confidence");

        // null 방지 + 공백 제거 + 대문자 변환
        const confidenceValue = (result.confidence || "LOW")
            .trim()
            .toUpperCase();

        confidence.textContent = confidenceValue;

        // 기존 색상 클래스 제거
        confidence.classList.remove(
            "confidence-high",
            "confidence-medium",
            "confidence-low"
        );

        // 신뢰도에 따라 색상 변경
        switch (confidenceValue) {

            case "HIGH":
                confidence.classList.add("confidence-high");
                break;

            case "MEDIUM":
                confidence.classList.add("confidence-medium");
                break;

            default:
                confidence.classList.add("confidence-low");
        }

    } catch (error) {

        console.error(error);

        alert("메뉴 추천 중 오류가 발생했습니다.");

    }

}