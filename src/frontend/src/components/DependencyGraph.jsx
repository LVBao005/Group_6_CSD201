import { useMemo, useState, useEffect, useCallback } from 'react';
import ReactFlow, { Controls, MarkerType, applyNodeChanges } from 'react-flow-renderer';
import 'react-flow-renderer/dist/style.css';

const columnOrder = ['TODO', 'DOING', 'DONE'];
const columnGap = 300;
const rowGap = 110;

const buildNodes = (board) => {
  const nodes = [];
  columnOrder.forEach((column, colIndex) => {
    const tasks = board[column] ?? [];
    tasks.forEach((task, taskIndex) => {
      nodes.push({
        id: String(task.id),
        data: {
          label: (
            <div className="rounded-lg border border-slate-700 bg-slate-900/60 p-2 text-[11px]">
              <p className="font-semibold text-slate-100">{task.title}</p>
              <p className="text-xs text-slate-400">#{task.id}</p>
            </div>
          ),
        },
        position: {
          x: colIndex * columnGap,
          y: taskIndex * rowGap,
        },
        sourcePosition: 'bottom',
        targetPosition: 'top',
      });
    });
  });
  return nodes;
};

const buildEdges = (dependencies) => {
  const edges = [];
  Object.entries(dependencies || {}).forEach(([source, targets]) => {
    targets.forEach((target) => {
      edges.push({
        id: `edge-${source}-${target}`,
        source: String(source),
        target: String(target),
        animated: true,
        markerEnd: { type: MarkerType.ArrowClosed },
      });
    });
  });
  return edges;
};

export default function DependencyGraph({ board = {}, dependencies = {}, cycleDetected }) {
  const [nodes, setNodes] = useState([]);
  const edges = useMemo(() => buildEdges(dependencies), [dependencies]);

  useEffect(() => {
    setNodes(buildNodes(board));
  }, [board]);

  const onNodesChange = useCallback(
    (changes) => setNodes((nds) => applyNodeChanges(changes, nds)),
    []
  );

  return (
    <section className="space-y-3 rounded-3xl border border-slate-800 bg-gradient-to-br from-slate-900/60 to-slate-900/90 p-5">
      <header className="flex items-center justify-between text-sm text-slate-300">
        <div>
          <h2 className="text-base font-semibold">Đồ thị phụ thuộc</h2>
          <p className="text-xs text-slate-500">Minh họa chuỗi công việc theo thứ tự cần hoàn thành.</p>
        </div>
        <span className={`rounded-full px-3 py-1 text-[11px] ${cycleDetected ? 'bg-rose-500/20 text-rose-200' : 'bg-emerald-500/10 text-emerald-200'}`}>
          {cycleDetected ? 'Đã phát hiện vòng lặp' : 'Không có vòng lặp'}
        </span>
      </header>
      <div className="relative h-[360px] rounded-2xl border border-slate-800 bg-black/40">
        {nodes.length === 0 ? (
          <div className="flex h-full items-center justify-center text-xs text-slate-500">
            Chưa có nhiệm vụ để dựng đồ thị phụ thuộc.
          </div>
        ) : (
          <ReactFlow 
            nodes={nodes} 
            edges={edges} 
            onNodesChange={onNodesChange}
            fitView 
            fitViewOptions={{ padding: 0.4 }} 
            nodesDraggable={true}
          >
            <Controls showInteractive={true} showZoom={true} />
          </ReactFlow>
        )}
      </div>
    </section>
  );
}
