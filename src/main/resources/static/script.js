document.addEventListener('DOMContentLoaded', () => {
    // 페이지 로드 시 유저 목록 불러오기
    fetchUsers();

    // 폼 제출 이벤트 리스너 추가
    const form = document.getElementById('userForm');
    if (form) {
        form.addEventListener('submit', createUser);
    }
});

// 1. 전체 사용자 목록 불러오기 (온라인 상태 추가 버전)
async function fetchUsers() {
    try {
        const response = await fetch('/api/user/findAll');
        const users = await response.json();

        const listContainer = document.getElementById('userList');
        listContainer.innerHTML = '';

        for (const user of users) {
            const userDiv = document.createElement('div');
            userDiv.className = 'user-item';

            let imgHtml = '<div class="avatar-placeholder">이미지 없음</div>';

            if (user.profileId) {
                const imageUrl = await fetchProfileImage(user.profileId);
                if (imageUrl) {
                    imgHtml = `<img src="${imageUrl}" class="avatar" alt="profile">`;
                }
            }

            // ⭐ 여기서 status-badge div를 다시 추가했어요!
            userDiv.innerHTML = `
                ${imgHtml} 
                <div class="info">
                    <div class="name">${user.username}</div>
                    <div class="email">${user.email}</div>
                </div>
                <div class="status-badge">온라인</div>
            `;
            listContainer.appendChild(userDiv);
        }
    } catch (error) {
        console.error('사용자 목록 불러오기 실패:', error);
    }
}

// 2. 프로필 이미지 데이터 가져와서 변환하기
async function fetchProfileImage(binaryContentId) {
    try {
        const response = await fetch(`/api/binaryContent/find?binaryContentId=${binaryContentId}`);
        if (!response.ok) return null;

        const binaryContent = await response.json();

        // ⭐ 핵심: 'bytes' 데이터가 문자열일 경우, 불필요한 모든 문자를 제거합니다.
        // 데이터의 앞뒤 공백, 중간에 섞인 줄바꿈(\n, \r) 등을 모두 제거!
        const base64String = binaryContent.bytes.replace(/[\r\n\s]+/g, '');

        // 이제 깐깐한 atob도 통과할 수 있습니다.
        const byteCharacters = atob(base64String);
        const byteNumbers = new Array(byteCharacters.length);

        for (let i = 0; i < byteCharacters.length; i++) {
            byteNumbers[i] = byteCharacters.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);

        const blob = new Blob([byteArray], { type: binaryContent.contentType });
        return URL.createObjectURL(blob);
    } catch (e) {
        console.error("이미지 변환 실패! 에러 내용:", e);
        return null;
    }
}

// 3. 사용자 등록 (이미지 포함)
async function createUser(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);

    try {
        const response = await fetch('/api/user/create', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            alert("등록 성공!");
            form.reset();
            // 등록 직후 목록 새로고침
            await fetchUsers();
        } else {
            const errorData = await response.json();
            alert(`등록 실패: ${errorData.message || '알 수 없는 오류'}`);
        }
    } catch (error) {
        console.error('등록 중 에러 발생:', error);
    }
}