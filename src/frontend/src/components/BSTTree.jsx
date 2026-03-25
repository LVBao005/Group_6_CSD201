import { useMemo } from 'react';
import ReactFlow, { Controls, MarkerType } from 'react-flow-renderer';
import 'react-flow-renderer/dist/style.css';

const nodeHorizontalGap = 140;
const nodeVerticalGap = 110;

const layoutTree = (root) => {
  const nodes = [];
  const edges = [];
  let counter = 0;
  const positions = new Map();

  const assign = (node, depth = 0) => {
    if (!node) return;
    assign(node.left, depth + 1);
    positions.set(node.id, { x: counter * nodeHorizontalGap, y: depth * nodeVerticalGap });
    counter += 1;
    assign(node.right, depth + 1);
  };

  assign(root);

  const buildNodes = (node) => {
    if (!node) return;
    const pos = positions.get(node.id) || { x: 0, y: 0 };
    nodes.push({
      id: String(node.id),
      position: { x: pos.x, y: pos.y },
      data: { label: `#${node.id}` },
      style: {
        width: 60,
        height: 60,
        borderRadius: '50%',
        border: '2px solid #94a3b8',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        background: '#0f172a',
        color: '#e2e8f0',
        fontWeight: 600,
      },
      sourcePosition: 'bottom',
      targetPosition: 'top',
    });
    if (node.left) {
    edges.push({
        id: `edge-${node.id}-${node.left.id}`,
        source: String(node.id),
        target: String(node.left.id),
        markerStart: { type: MarkerType.ArrowClosed },
        markerEnd: undefined,
        animated: true,
        style: { stroke: '#94a3b8', strokeWidth: 1.5 },
      });
    }
    if (node.right) {
      edges.push({
        id: `edge-${node.id}-${node.right.id}`,
        source: String(node.id),
        target: String(node.right.id),
        markerStart: { type: MarkerType.ArrowClosed },
        markerEnd: undefined,
        animated: true,
        style: { stroke: '#94a3b8', strokeWidth: 1.5 },
      });
    }
    buildNodes(node.left);
    buildNodes(node.right);
  };

  buildNodes(root);
  return { nodes, edges };
};

export default function BSTTree({ root, highlightId }) {
  const { nodes, edges } = useMemo(() => {
    if (!root) {
      return { nodes: [], edges: [] };
    }
    return layoutTree(root);
  }, [root]);

  const styledNodes = useMemo(
    () =>
      nodes.map((node) => ({
        ...node,
        style: {
          ...node.style,
          borderColor: highlightId && Number(node.id) === highlightId ? '#38bdf8' : node.style.border,
          background: highlightId && Number(node.id) === highlightId ? '#0ea5e9' : node.style.background,
        },
      })),
    [nodes, highlightId]
  );

  if (!root) {
    return (
      <div className="flex h-48 items-center justify-center text-xs text-slate-500">
        Chưa có cây BST để hiển thị.
      </div>
    );
  }

  return (
    <ReactFlow
      nodes={styledNodes}
      edges={edges}
      fitView
      fitViewOptions={{ padding: 0.8 }}
      defaultZoom={1}
      minZoom={0.5}
      nodesDraggable={false}
      nodesConnectable={false}
      elementsSelectable={false}
    >
      <Controls showInteractive={true} showZoom={true} />
    </ReactFlow>
  );
}
