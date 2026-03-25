# Tomcat Monitoring Links (TM)
Use the following URLs to verify the backend deployment and inspect any runtime errors.

1. **Task API listing** – `http://localhost:8080/mini-jira-server/api/tasks` (GET should return the full board and dependency info).
2. **Application landing page** – `http://localhost:8080/mini-jira-server/` (should show a JSON welcome or root servlet message).

3. **Move task endpoint (quick test)** – `POST http://localhost:8080/mini-jira-server/api/tasks/move` with a JSON payload like `{ "taskId": 3, "targetColumn": "DONE" }` to verify task migrations.
4. **Tomcat Manager (if enabled)** – `http://localhost:8080/manager/html` (requires `manager-gui` credentials defined in `conf/tomcat-users.xml`).
5. **Logs** – watch `logs/catalina.out` or `logs/localhost.<date>.log` for stack traces and runtime errors.

If any endpoint returns 404/500, check the logs, verify `db.properties`, and confirm the WAR name matches `mini-jira-server`.

#### Kịch bản báo cáo dự án
1. **Giới thiệu tổng quan** – trình bày kiến trúc gồm LinkedList cho bảng To Do/Doing/Done, BST để tra cứu theo mã ID và đồ thị có hướng để xác định phụ thuộc giữa các task theo Theme 4 Mini Jira.
2. **Chạy thử & xác thực dữ liệu** – sử dụng `tasks.sql` để sinh `jiradb`, khởi động Tomcat, xem `/mini-jira-server/api/tasks` cho thấy các cột không còn trống rồi thao tác kéo thả để đảm bảo node thực sự di chuyển giữa các linked list.
3. **Kiểm tra phụ thuộc** – chèn nhiều task có trường `depends_on`, gọi API để kiểm tra `dependencies` và `cycleDetected`, đồng thời quan sát giao diện React Flow hiển thị đồ thị phụ thuộc, đảm bảo không bị vòng lặp hoặc nếu có thì có cảnh báo rõ ràng.
4. **Kết luận & hành động tiếp theo** – xác nhận toàn bộ chức năng CRUD (đọc board, cập nhật trạng thái qua POST /api/tasks/move) đang hoạt động, khuyến nghị xem `logs/catalina.out` nếu lỗi, và đề xuất bước tiếp (mở rộng CRUD thêm tạo/xóa nếu cần thiết).

#### Luồng chặn task do phụ thuộc
- Hệ thống đọc bảng `tasks` để xây dựng `MyGraph` với cạnh từ task tiền đề sang task phụ thuộc.  
- Khi backend nhận lệnh `POST /api/tasks/move` chuyển Task 2 sang cột mới, `MoveTaskServlet` gọi `TaskManager.blockingParents(2)` để lấy tập các task chặn (ở trạng thái chưa hoàn tất).  
- Nếu tập này không rỗng (ví dụ Task 1 vẫn ở cột Cần làm), servlet trả HTTP 409 cùng thông báo rõ ID parent đang chặn và không thao tác cắt-nối linked list.  
- Frontend đọc mã lỗi và hiển thị toast bên bảng Kanban: “Task này bị khóa bởi task tiền đề #1 chưa xong”, đồng thời task card được render lại tại cột cũ sau khi board reload lại.  
- **Vị trí thực thi** – hàm kiểm tra `TaskManager.blockingParents` nằm tại `src/backend/src/main/java/com/minijira/TaskManager.java:118`, còn servlet gọi và trả lỗi nằm ở `src/backend/src/main/java/com/minijira/MoveTaskServlet.java:41-50` (có gọi đến `TaskManager.moveTask` tại dòng 50 để cập nhật danh sách linked list và BST sau khi kiểm tra phụ thuộc).  
