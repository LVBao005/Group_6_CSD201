# Mini Jira (Theme 4)

This repository hosts the **Mini Jira** task-management system split into two independent apps:

- **mini-jira-server** – a Maven/Servlet backend in `Java Servlet 4.0`, using **Gson**, **JDBC**, and the required data structures (`MyLinkedList`, `MyBST`, `MyGraph`).
- **mini-jira-client** – a **React + Vite** frontend with **Tailwind CSS**, `@dnd-kit`, `react-flow`, `Axios`, and `Lucide` for icons.

## Architecture highlights
1. **Task board (Linked Lists)** – each column (To Do / Doing / Done) is managed by `MyLinkedList`, so moving a task removes it from one list and inserts it into another while keeping the original node references.
2. **Task lookup (BST)** – `MyBST` stores all tasks keyed by `id` to guarantee logarithmic search times for tasks by ID.
3. **Dependencies (Graph)** – `MyGraph` maintains directed edges between tasks; when Task A depends on Task B, the graph is used to validate execution order and prevent cycles.

## Backend (mini-jira-server)
1. Import the Maven module (JDK 11+).
2. Configure `resources/db.properties` to point to your JDBC database (MySQL/PostgreSQL).
3. Seed the schema and 50 starter tasks via `tasks.sql` at the repository root.
4. Build and deploy the WAR: `mvn clean package` produces `target/mini-jira-server.war`.
5. Deploy to Tomcat (see `TM.md` for quick URLs) and ensure `localhost:8080/mini-jira-server/api/tasks` returns JSON.

## Frontend (mini-jira-client)
```
cd mini-jira-client
npm install
npm run dev
```
Vite proxies `/api` requests to `http://localhost:8080` so the React app can talk to the servlet backend without CORS issues.

## Running locally
1. Start the backend WAR under Tomcat (port 8080).
2. Seed the DB using `tasks.sql`.
3. Launch the frontend (`npm run dev`) and open the Vite URL.
4. Use the board to drag cards between columns and open the dependency graph view powered by `react-flow`.

## Useful scripts
- `tasks.sql` – schema + 50 initial tasks with mixed statuses and dependencies.
- Backend data structures: `MyLinkedList`, `MyBST`, `MyGraph` for the course focus, plus servlets (`TaskServlet`, `MoveTaskServlet`).

## Next steps
Adjust JDBC connection, add authentication, and expand the React board with filters/notifications as needed.
