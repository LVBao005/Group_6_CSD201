# Mini Jira - Quản lý Công việc (Nhóm 5)

Group:
| Name | MSSV |
|------|------|
| Lê Văn Bảo | QE190130 |
| Trần Anh Quân | QE180116 |


Ứng dụng quản lý công việc (Mini Jira) dựa trên Java, minh họa các cấu trúc dữ liệu giải thuật nâng cao: **Linked List**, **Binary Search Tree (BST)**, và **Đồ thị (Graph)**.

**Pair Programming:** Nhóm 5 - CSD201

## Tính năng (DSA)

| Module | Cấu trúc dữ liệu | Chức năng |
|--------|------------------|-----------|
| **Kanban Board** | Linked List | Các cột To Do, Doing, Done được quản lý bằng danh sách liên kết. Di chuyển task là thao tác cắt/dán node giữa các list. |
| **Task Lookup** | BST | Tìm kiếm nhanh nhiệm vụ theo ID. Hỗ trợ xáo trộn (Shuffle) để cân bằng cây tự động. |
| **Dependency Graph** | Graph | Đồ thị có hướng để quản lý phụ thuộc (Task A phải xong mới làm được Task B). Kiểm tra chu trình (Cycle detection). |

## Yêu cầu hệ thống

- **JDK 17** trở lên
- **Maven** (quản lý backend)
- **Node.js & npm** (quản lý frontend)
- **MySQL** (cơ sở dữ liệu)

## Cách chạy

### 1. Cấu hình Database
- Sử dụng file SQL trong `src/backend/tasks.sql` (hoặc `CSDdb.sql` tùy phiên bản) để tạo database `jiradb`.
- Cấu hình kết nối trong `src/backend/src/main/resources/db.properties` (mặc định: port 3307, root/123456).

### 2. Chạy Backend (Java Servlet)
```bash
cd src/backend
mvn clean package
# Deploy file .war vào Tomcat hoặc Jetty
```

### 3. Chạy Frontend (React + Vite)
```bash
cd src/frontend
npm install
npm run dev
```

### 4. Truy cập
Mở trình duyệt: **http://localhost:5173** (mặc định của Vite)

## Hướng dẫn Demo

### Kanban Board (Linked List)
1. Kéo thả các thẻ nhiệm vụ giữa các cột **To Do**, **Doing**, **Done**.
2. Khi chuyển từ **To Do** sang **Doing**, hệ thống sẽ kiểm tra Graph: Nếu các Task tiền đề chưa xong (Done), hành động sẽ bị chặn và hiển thị thông báo lỗi.

### Task Search & BST (BST)
1. Chuyển sang tab **Cây BST theo ID**.
2. Xem cấu trúc cây nhị phân tìm kiếm được vẽ trực quan.
3. **Tìm kiếm:** Nhập ID task vào ô tìm kiếm để highlight node trên cây và xem chi tiết.
4. **Xáo trộn (Random):** Nhấn nút để thay đổi thứ tự chèn, giúp cân bằng lại cây BST một cách tự nhiên.

### Dependency Graph (Graph)
1. Chuyển sang tab **Sơ đồ phụ thuộc (Graph)**.
2. Xem các mũi tên biểu thị quan hệ phụ thuộc giữa các Task.
3. Node có thể di chuyển (drag) để sắp xếp sơ đồ dễ nhìn hơn.

## Cấu trúc project

```
src/
├── backend/
│   ├── src/main/java/com/minijira/
│   │   ├── MyLinkedList.java     # Logic Linked List
│   │   ├── MyBST.java            # Logic Binary Search Tree
│   │   ├── MyGraph.java          # Logic Directed Graph
│   │   ├── TaskManager.java      # Lớp điều phối (Manager)
│   │   └── *Servlet.java         # Các API Endpoints
├── frontend/
│   ├── src/
│   │   ├── App.jsx               # Giao diện chính & Logic API
│   │   └── components/           # Các component vẽ Graph/BST
```

## Chi tiết kỹ thuật (DSA)

| Cấu trúc | Độ phức tạp | Mô tả |
|----------|-------------|-------|
| **MyLinkedList** | O(1) for Head/Tail | Quản lý tuần tự các task trong từng trạng thái. |
| **MyBST** | O(log n) | Tìm kiếm/Chèn task theo ID hiệu quả. |
| **MyGraph** | O(V + E) | Duyệt DFS/BFS để kiểm tra phụ thuộc và chu trình. |

## Công nghệ sử dụng

- **Backend:** Java 17, Jakarta Servlet, MySQL, Gson.
- **Frontend:** React, Vite, React Flow (vẽ đồ thị), Axios, Tailwind CSS.
- **Build:** Maven, npm.

## API Endpoints

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/tasks` | Lấy dữ liệu board & quan hệ phụ thuộc |
| POST | `/api/tasks/manage` | Thêm, sửa, xóa nhiệm vụ |
| POST | `/api/tasks/move` | Di chuyển node giữa các cột (Linked List + Graph check) |
| GET | `/api/tasks/search?id=` | Tìm kiếm task theo ID (BST) |
| GET | `/api/tasks/bst` | Lấy dữ liệu cấu trúc cây BST |
| GET | `/api/tasks/bst/shuffle` | Xáo trộn thứ tự chèn BST |

## Môn học

Dự án thực hiện cho môn **CSD201 - Data Structures and Algorithms**.
Triết lý: DSA kết hợp AI-Assisted Coding (PBL).

## GitHub
[https://github.com/LVBao005/Group_6_CSD201](https://github.com/LVBao005/Group_6_CSD201)
