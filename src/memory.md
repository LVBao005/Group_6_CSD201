# Project Memory Context: Mini Jira (Task Management)

## 🎯 Role & Objective
- **Course**: CSD201 - Data Structures and Algorithms (PBL + AI-Assisted)
- **Role**: Junior Developer (AI Agent).
- **Tech Lead**: User (Architect/Reviewer).
- **Core Objective**: Build a complete Task Management system (Mini Jira) using custom-built data structures.

## 🛠 Tech Stack & Constraints
- **Language**: Java.
- **Persistence**: MySQL (jiradb).
- **Frontend**: React (Vite) + Tailwind CSS.
- **Backend**: Java Servlets (Tomcat).
- **CRITICAL CONSTRAINT**: Core data structures (Linked List, BST, Graph) must be **custom-built**. Do not use `java.util` collections (like `LinkedList`, `TreeMap`, etc.) for the main logic.

## 🧩 Core Data Structures
1.  **Linked List**: Manages task columns (`TODO`, `DOING`, `DONE`). Moving tasks involves cutting/pasting nodes between lists.
2.  **BST (Binary Search Tree)**: Used for fast O(log n) task lookup by ID.
3.  **Directed Graph**: Manages task dependencies (Task A → Task B). Used for logical validation (blocking tasks) and cycle detection.

## 🗺 Implementation Roadmap
### Sprint 1: Linear DS (Weeks 1-3)
- [x] Define `Task` & `Node` classes.
- [x] Implement `MyLinkedList` (Custom).
- [x] Logic for moving tasks between columns.
- [x] Unit Tests for `NullPointerException` & edge cases.

### Sprint 2: Searching & Performance (Weeks 4-6)
- [x] Implement `MyBST` (Custom).
- [x] Synchronize BST with LinkedList operations.
- [x] Benchmark: Compare searching 10,000 items (O(n) vs O(log n)).

### Sprint 3: Graphs & Logic (Weeks 7-9)
- [x] Implement `MyGraph` using Adjacency List.
- [x] Cycle Detection algorithm.
- [x] Dependency validation (e.g., blocking `MoveTask` if parent is incomplete).
- [x] File I/O for graph data.

### Sprint 4: Finalization (Week 10)
- [x] UI Integration (React Kanban Board + Dependency Graph visualization).
- [x] Code Cleaning & Refactoring.
- [x] Final Report & Presentation.

## 📝 Compliance & AI Logs
- **AI Interaction Log**: Mandatory for each Assignment. Must document Prompts, AI Code, and manual fixes.
- **Trust but Verify**: Tech Lead must be able to explain every line of code.
- **GitHub**: Continuous commits required (min 10 commits per phase).
