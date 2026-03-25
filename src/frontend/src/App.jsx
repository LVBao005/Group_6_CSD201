import { useEffect, useMemo, useState } from 'react';
import axios from 'axios';
import { RefreshCw } from 'lucide-react';
import Board from './components/Board';
import DependencyGraph from './components/DependencyGraph';
import BSTTree from './components/BSTTree';

const columnOrder = ['TODO', 'DOING', 'DONE'];
const columnLabels = {
  TODO: 'Cần làm',
  DOING: 'Đang làm',
  DONE: 'Hoàn tất',
};

const statusOptions = ['TODO', 'DOING', 'DONE'];
const priorityOptions = ['Thấp', 'Trung bình', 'Cao'];

const initialForm = {
  id: '',
  title: '',
  description: '',
  status: 'TODO',
  priority: 'Trung bình',
  estimateHours: 2,
  dependsOn: '',
};

export default function App() {
  const [board, setBoard] = useState({});
  const [dependencies, setDependencies] = useState({});
  const [cycle, setCycle] = useState(false);
  const [loading, setLoading] = useState(true);
  const [treeRoot, setTreeRoot] = useState(null);
  const [activeTab, setActiveTab] = useState('board');
  const [form, setForm] = useState(initialForm);
  const [message, setMessage] = useState('');
  const [editMode, setEditMode] = useState(false);
  const [searchId, setSearchId] = useState('');
  const [searchResult, setSearchResult] = useState(null);
  const [searchError, setSearchError] = useState('');

  const flattenTasks = useMemo(() => Object.values(board).flat(), [board]);
  const taskList = useMemo(() => [...flattenTasks].sort((a, b) => a.id - b.id), [flattenTasks]);

  const stats = useMemo(() => {
    const total = flattenTasks.length;
    const open = (board.TODO?.length ?? 0) + (board.DOING?.length ?? 0);
    const done = board.DONE?.length ?? 0;
    return [
      { label: 'Tổng số', value: total, help: 'Tổng các thẻ hiện có' },
      { label: 'Cần theo dõi', value: open, help: 'Cột Cần làm + Đang làm' },
      { label: 'Đã xong', value: done, help: 'Cột Hoàn tất' },
    ];
  }, [board, flattenTasks]);

  const fetchBoard = async () => {
    setLoading(true);
    try {
      const response = await axios.get('/api/tasks');
      setBoard(response.data.columns);
      setDependencies(response.data.dependencies);
      setCycle(response.data.cycleDetected);
    } catch (err) {
      setMessage('Không thể tải dữ liệu board.');
    } finally {
      setLoading(false);
    }
  };

  const fetchTree = async () => {
    try {
      const response = await axios.get('/api/tasks/bst');
      setTreeRoot(response.data.root);
    } catch (err) {
      setTreeRoot(null);
    }
  };

  const handleShuffleBST = async () => {
    setLoading(true);
    try {
      const response = await axios.get('/api/tasks/bst/shuffle');
      setTreeRoot(response.data.root);
      setMessage('Đã xáo trộn thứ tự chèn BST để cân bằng hơn.');
    } catch (err) {
      setMessage('Không thể xáo trộn cây BST.');
    } finally {
      setLoading(false);
    }
  };

  const fetchAll = async () => {
    await fetchBoard();
    await fetchTree();
  };

  useEffect(() => {
    fetchAll();
  }, []);

  const handleMove = async (taskId, targetColumn) => {
    try {
      await axios.post('/api/tasks/move', { taskId, targetColumn });
      await fetchAll();
    } catch (err) {
      const msg = err.response?.data?.message ?? 'Không thể cập nhật trạng thái nhiệm vụ.';
      setMessage(msg);
      await fetchBoard();
    }
  };

  useEffect(() => {
    if (!message) {
      return;
    }
    const timer = setTimeout(() => setMessage(''), 5000);
    return () => clearTimeout(timer);
  }, [message]);

  const handleCreate = async (event) => {
    event.preventDefault();
    setMessage('');
    try {
      const payload = {
        action: 'create',
        task: {
          title: form.title.trim(),
          description: form.description.trim(),
          status: form.status,
          priority: form.priority,
          estimateHours: Number(form.estimateHours) || 2,
          dependsOn: form.dependsOn ? Number(form.dependsOn) : null,
        },
      };
      await axios.post('/api/tasks/manage', payload);
      setForm(initialForm);
      setMessage('Đã thêm nhiệm vụ mới.');
      await fetchAll();
    } catch (err) {
      setMessage('Không thể thêm nhiệm vụ.');
    }
  };

  const handleUpdate = async (event) => {
    event.preventDefault();
    setMessage('');
    try {
      const payload = {
        action: 'update',
        task: {
          id: Number(form.id),
          title: form.title.trim(),
          description: form.description.trim(),
          status: form.status,
          priority: form.priority,
          estimateHours: Number(form.estimateHours) || 2,
          dependsOn: form.dependsOn ? Number(form.dependsOn) : null,
        },
      };
      await axios.post('/api/tasks/manage', payload);
      setMessage('Đã cập nhật nhiệm vụ.');
      setForm(initialForm);
      setEditMode(false);
      await fetchAll();
    } catch (err) {
      setMessage('Không thể cập nhật nhiệm vụ.');
    }
  };

  const handleDelete = async (id) => {
    setMessage('');
    try {
      await axios.post('/api/tasks/manage', { action: 'delete', taskId: id });
      setMessage(`Đã xóa nhiệm vụ #${id}.`);
      await fetchAll();
    } catch (err) {
      setMessage('Không thể xóa nhiệm vụ này.');
    }
  };

  const handleEdit = (task) => {
    setEditMode(true);
    setForm({
      id: task.id,
      title: task.title,
      description: task.description,
      status: task.status,
      priority: task.priority,
      estimateHours: task.estimateHours,
      dependsOn: task.dependsOn ?? '',
    });
  };

  const handleSearch = async (event) => {
    if (event) {
      event.preventDefault();
    }
    if (!searchId) {
      setSearchError('Nhập ID để tìm nhiệm vụ.');
      setSearchResult(null);
      return;
    }
    setSearchError('');
    try {
      const response = await axios.get(`/api/tasks/search?id=${searchId}`);
      setSearchResult(response.data.task);
    } catch (err) {
      setSearchResult(null);
      const msg = err.response?.data?.message ?? 'Không tìm thấy nhiệm vụ';
      setSearchError(msg);
    }
  };

  const tabs = [
    { key: 'board', label: 'Bảng Kanban' },
    { key: 'list', label: 'Danh sách nhiệm vụ' },
    { key: 'bst', label: 'Cây BST theo ID' },
  ];

  return (
    <div className="min-h-screen p-6 relative">
      {message && (
        <div className="fixed left-1/2 top-4 z-40 w-[calc(100%-3rem)] -translate-x-1/2 rounded-2xl border border-rose-500 bg-rose-500/90 px-4 py-3 text-center text-sm font-semibold text-white shadow-lg shadow-black/30">
          {message}
        </div>
      )}
      <div className="mx-auto flex max-w-6xl flex-col gap-6 rounded-3xl border border-slate-800 bg-slate-900/70 p-6 shadow-2xl shadow-slate-950/70">
        <header className="flex flex-col gap-6 lg:flex-row lg:items-center lg:justify-between">
          <div>
            <p className="text-xs uppercase tracking-[0.4em] text-slate-400">Mini Jira</p>
            <h1 className="text-4xl font-semibold text-white">Quản lý công việc & đồ thị phụ thuộc</h1>
            <p className="mt-2 max-w-2xl text-sm text-slate-300">
              Kéo thả thẻ, quản lý CRUD nhiệm vụ và kiểm tra logic phụ thuộc qua cây BST.
            </p>
          </div>
          <div className="flex flex-wrap items-center gap-3">
            <button
              onClick={fetchAll}
              className="inline-flex items-center gap-2 rounded-full border border-slate-600/70 bg-slate-800/60 px-4 py-2 text-sm text-slate-100 transition hover:border-cyan-400 hover:text-cyan-200"
            >
              <RefreshCw className="h-4 w-4" />
              Cập nhật
            </button>
            <span className="text-xs text-slate-400">
              Trạng thái: {loading ? 'Đang tải…' : cycle ? 'Phát hiện vòng lặp' : 'Ổn định, không có vòng lặp'}
            </span>
          </div>
        </header>

        {message && <div className="rounded-2xl border border-slate-700 bg-slate-950/80 p-3 text-sm text-slate-200">{message}</div>}

        <div className="grid gap-4 lg:grid-cols-3">
          {stats.map((stat) => (
            <div key={stat.label} className="rounded-2xl border border-slate-800 bg-gradient-to-br from-slate-900/40 to-slate-900/70 p-4">
              <p className="text-xs uppercase tracking-widest text-slate-500">{stat.label}</p>
              <p className="text-3xl font-semibold">{stat.value}</p>
              <p className="text-xs text-slate-500">{stat.help}</p>
            </div>
          ))}
        </div>

        <div className="flex flex-wrap gap-2">
          {tabs.map((tab) => (
            <button
              key={tab.key}
              onClick={() => setActiveTab(tab.key)}
              className={`rounded-full px-4 py-2 text-sm ${
                activeTab === tab.key ? 'border-cyan-400 bg-cyan-500/10 text-white' : 'border border-slate-700 text-slate-300'
              }`}
            >
              {tab.label}
            </button>
          ))}
        </div>

        {activeTab === 'board' && (
          <>
            <Board
              board={board}
              onMove={handleMove}
              disabled={loading}
              columnOrder={columnOrder}
              columnLabels={columnLabels}
            />
            <DependencyGraph dependencies={dependencies} board={board} cycleDetected={cycle} />
          </>
        )}

        {activeTab === 'list' && (
          <section className="space-y-6 rounded-2xl border border-slate-800 bg-slate-900/80 p-5">
            <form className="grid gap-4 md:grid-cols-2" onSubmit={editMode ? handleUpdate : handleCreate}>
              {editMode && (
                <input
                  type="number"
                  value={form.id}
                  readOnly
                  className="col-span-full rounded-xl border border-slate-700 bg-slate-950/60 px-3 py-2 text-sm"
                />
              )}
              <input
                value={form.title}
                onChange={(e) => setForm({ ...form, title: e.target.value })}
                placeholder="Tiêu đề nhiệm vụ"
                className="rounded-xl border border-slate-700 bg-slate-950/60 px-3 py-2 text-sm"
                required
              />
              <select
                value={form.status}
                onChange={(e) => setForm({ ...form, status: e.target.value })}
                className="rounded-xl border border-slate-700 bg-slate-950/60 px-3 py-2 text-sm"
              >
                {statusOptions.map((status) => (
                  <option key={status} value={status}>
                    {status}
                  </option>
                ))}
              </select>
              <textarea
                value={form.description}
                onChange={(e) => setForm({ ...form, description: e.target.value })}
                placeholder="Mô tả"
                className="col-span-full rounded-xl border border-slate-700 bg-slate-950/60 px-3 py-2 text-sm"
              />
              <select
                value={form.priority}
                onChange={(e) => setForm({ ...form, priority: e.target.value })}
                className="rounded-xl border border-slate-700 bg-slate-950/60 px-3 py-2 text-sm"
              >
                {priorityOptions.map((priority) => (
                  <option key={priority} value={priority}>
                    {priority}
                  </option>
                ))}
              </select>
              <input
                type="number"
                value={form.estimateHours}
                min={1}
                onChange={(e) => setForm({ ...form, estimateHours: Number(e.target.value) })}
                placeholder="Ước lượng giờ"
                className="rounded-xl border border-slate-700 bg-slate-950/60 px-3 py-2 text-sm"
              />
              <input
                type="number"
                value={form.dependsOn}
                onChange={(e) => setForm({ ...form, dependsOn: e.target.value })}
                placeholder="Phụ thuộc vào ID (tuỳ chọn)"
                className="rounded-xl border border-slate-700 bg-slate-950/60 px-3 py-2 text-sm"
              />
              <button
                type="submit"
                className="col-span-full rounded-2xl bg-cyan-500/70 px-4 py-2 text-sm font-semibold text-white transition hover:bg-cyan-500"
              >
                {editMode ? 'Cập nhật nhiệm vụ' : 'Thêm nhiệm vụ'}
              </button>
              {editMode && (
                <button
                  type="button"
                  onClick={() => {
                    setEditMode(false);
                    setForm(initialForm);
                  }}
                  className="col-span-full rounded-2xl border border-slate-700 bg-slate-950/60 px-4 py-2 text-sm font-semibold text-slate-200 transition hover:border-rose-500"
                >
                  Huỷ chỉnh sửa
                </button>
              )}
            </form>
            <div className="overflow-auto rounded-2xl border border-slate-800 bg-slate-950/70">
              <table className="min-w-full text-left text-xs text-slate-300">
                <thead className="text-[10px] uppercase tracking-wide text-slate-500">
                  <tr>
                    <th className="px-3 py-2">ID</th>
                    <th className="px-3 py-2">Tiêu đề</th>
                    <th className="px-3 py-2">Trạng thái</th>
                    <th className="px-3 py-2">Ưu tiên</th>
                    <th className="px-3 py-2">Giờ</th>
                    <th className="px-3 py-2">Phụ thuộc</th>
                    <th className="px-3 py-2">Hành động</th>
                  </tr>
                </thead>
                <tbody>
                  {taskList.map((task) => (
                    <tr key={task.id} className="border-t border-slate-800">
                      <td className="px-3 py-2 text-slate-300">#{task.id}</td>
                      <td className="px-3 py-2">{task.title}</td>
                      <td className="px-3 py-2">{task.status}</td>
                      <td className="px-3 py-2">{task.priority}</td>
                      <td className="px-3 py-2">{task.estimateHours}h</td>
                      <td className="px-3 py-2">{task.dependsOn ?? '-'}</td>
                      <td className="px-3 py-2 flex gap-2">
                        <button
                          onClick={() => handleEdit(task)}
                          className="rounded-full border border-cyan-500 px-3 py-1 text-[11px] text-cyan-200"
                        >
                          Sửa
                        </button>
                        <button
                          onClick={() => handleDelete(task.id)}
                          className="rounded-full border border-rose-500 px-3 py-1 text-[11px] text-rose-300"
                        >
                          Xóa
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </section>
        )}

        {activeTab === 'bst' && (
          <section className="space-y-4 rounded-2xl border border-slate-800 bg-slate-900/80 p-5">
            <div className="flex items-center justify-between">
              <div>
                <h3 className="text-lg font-semibold text-white">Cây BST theo ID</h3>
                <p className="text-xs text-slate-500">Cấu trúc cây dựa trên thứ tự chèn hiện tại</p>
              </div>
              <button
                onClick={handleShuffleBST}
                disabled={loading}
                className="inline-flex items-center gap-2 rounded-full border border-cyan-500/50 bg-cyan-500/10 px-4 py-2 text-xs font-semibold text-cyan-200 transition hover:bg-cyan-500 hover:text-white disabled:opacity-50"
              >
                <RefreshCw className={`h-3 w-3 ${loading ? 'animate-spin' : ''}`} />
                Xáo trộn (Random)
              </button>
            </div>
            <form className="flex flex-col gap-3 rounded-2xl border border-slate-800 bg-slate-950/70 p-4 sm:flex-row sm:items-center" onSubmit={handleSearch}>
              <div className="flex flex-col flex-1 gap-1">
                <label className="text-[10px] uppercase tracking-wide text-slate-400">Tìm task theo ID</label>
                <input
                  value={searchId}
                  onChange={(e) => setSearchId(e.target.value)}
                  type="number"
                  min="1"
                  placeholder="Nhập số ID"
                  className="w-full rounded-xl border border-slate-700 bg-slate-950/60 px-3 py-2 text-sm"
                />
              </div>
              <button
                type="submit"
                className="rounded-2xl bg-cyan-500/80 px-4 py-2 text-sm font-semibold text-white transition hover:bg-cyan-500"
              >
                Tìm task
              </button>
              <p className="text-xs text-rose-300">{searchError}</p>
            </form>
            {searchResult && (
              <div className="rounded-2xl border border-cyan-500/50 bg-cyan-500/10 p-3 text-sm text-slate-200">
                Tìm thấy: #{searchResult.id} · {searchResult.title}
              </div>
            )}
            <div className="overflow-hidden rounded-3xl border border-slate-800 bg-black/40 p-4">
              <div className="h-[360px]">
                <BSTTree root={treeRoot} highlightId={Number(searchResult?.id)} />
              </div>
            </div>
          </section>
        )}
      </div>
    </div>
  );
}
