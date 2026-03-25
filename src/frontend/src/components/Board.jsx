import { DndContext, useDraggable, useDroppable } from '@dnd-kit/core';
import { CSS } from '@dnd-kit/utilities';

const TaskCard = ({ task }) => {
  const { attributes, listeners, setNodeRef, transform, isDragging } = useDraggable({
    id: `task-${task.id}`,
    data: { taskId: task.id },
  });

  const style = {
    transform: CSS.Translate.toString(transform),
    opacity: isDragging ? 0.7 : 1,
  };

  return (
    <div
      ref={setNodeRef}
      style={style}
      className="rounded-2xl border border-slate-800 bg-gradient-to-br from-slate-900/70 to-slate-900/90 p-4 text-sm shadow-2xl shadow-slate-950/60 transition-all duration-200 hover:-translate-y-0.5"
      {...listeners}
      {...attributes}
    >
      <div className="text-sm font-semibold text-slate-100">{task.title}</div>
      <div className="text-xs text-slate-400">{task.description}</div>
      <div className="mt-3 flex items-center justify-between text-[11px] text-slate-400">
        <span>Mã #{task.id}</span>
        <span className="rounded-full bg-slate-800 px-2 py-0.5 text-[10px] uppercase tracking-wide">
          {task.priority}
        </span>
      </div>
    </div>
  );
};

const Column = ({ columnId, title, tasks }) => {
  const { setNodeRef, isOver } = useDroppable({ id: `column-${columnId}` });

  return (
    <div
      ref={setNodeRef}
      className={`flex min-h-[260px] flex-1 flex-col gap-4 rounded-3xl border border-slate-800 bg-slate-900/60 p-4 transition ${
        isOver ? 'border-cyan-400/80 bg-slate-900/90 shadow-[0_0_18px_rgba(14,165,233,0.35)]' : ''
      }`}
    >
      <div className="flex cursor-default items-center justify-between text-xs font-semibold text-slate-300">
        <span className="flex flex-col items-center gap-1 text-sm font-bold uppercase tracking-wide text-slate-100">
          <span className="text-lg">{title}</span>
        </span>
        <span className="text-[11px] text-slate-400">{tasks?.length ?? 0} thẻ</span>
      </div>
      <div className="flex grow flex-col gap-3">
        {tasks?.map((task) => (
          <TaskCard key={task.id} task={task} />
        ))}
        {tasks?.length === 0 && <div className="text-xs text-slate-500">Không có nhiệm vụ</div>}
      </div>
    </div>
  );
};

export default function Board({
  board = {},
  onMove,
  disabled,
  columnOrder = ['TODO', 'DOING', 'DONE'],
  columnLabels = {},
}) {
  const entries = columnOrder.map((columnId) => [columnId, board[columnId] ?? []]);

  const handleDragEnd = (event) => {
    if (disabled) return;
    const { active, over } = event;
    if (!over || !active) {
      return;
    }
    const targetColumn = over.id?.replace('column-', '');
    const taskId = active.data.current?.taskId;
    if (taskId && targetColumn) {
      onMove(taskId, targetColumn);
    }
  };

  return (
    <DndContext onDragEnd={handleDragEnd}>
      <div className="flex w-full flex-col gap-4 lg:flex-row lg:gap-6">
        {entries.map(([columnId, tasks]) => (
          <Column
            key={columnId}
            columnId={columnId}
            title={columnLabels[columnId] ?? columnId}
            tasks={tasks}
          />
        ))}
      </div>
    </DndContext>
  );
}
