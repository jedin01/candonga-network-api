import { ViewConfig } from "@vaadin/hilla-file-router/types.js";
import {
  Button,
  HorizontalLayout,
  VerticalLayout,
  Card,
  ProgressBar,
} from "@vaadin/react-components";
import {
  TaskService,
  CategoriaParagemService,
  ParagemService,
  RotaService,
} from "Frontend/generated/endpoints";
import { useSignal } from "@vaadin/hilla-react-signals";
import { Group, ViewToolbar } from "Frontend/components/ViewToolbar";
import { useEffect } from "react";

export const config: ViewConfig = {
  title: "Dashboard",
  menu: {
    icon: "vaadin:dashboard",
    order: 0,
    title: "Dashboard",
  },
};

interface SystemStats {
  tasks: {
    total: number;
    completed: number;
    pending: number;
    overdue: number;
    completionRate: number;
  };
  categorias: number;
  paragens: number;
  rotas: number;
  precoStats?: {
    precoMinimo: number;
    precoMaximo: number;
    precoMedio: number;
  };
  distanciaStats?: {
    distanciaMinima: number;
    distanciaMaxima: number;
    distanciaMedia: number;
  };
}

export default function DashboardView() {
  const stats = useSignal<SystemStats | null>(null);
  const loading = useSignal(false);
  const lastUpdate = useSignal<Date | null>(null);

  const loadSystemStats = async () => {
    loading.value = true;
    try {
      const [
        taskStats,
        categoriasCount,
        paragensCount,
        rotasCount,
        precoStats,
        distanciaStats,
      ] = await Promise.all([
        TaskService.getStatistics(),
        CategoriaParagemService.contarTotal(),
        ParagemService.contarTotal(),
        RotaService.contarTotal(),
        RotaService.obterEstatisticasPrecos(),
        RotaService.obterEstatisticasDistancias(),
      ]);

      stats.value = {
        tasks: {
          total: taskStats.totalTasks,
          completed: taskStats.completedTasks,
          pending: taskStats.pendingTasks,
          overdue: taskStats.overdueTasks,
          completionRate: taskStats.completionRate || 0,
        },
        categorias: categoriasCount,
        paragens: paragensCount,
        rotas: rotasCount,
        precoStats,
        distanciaStats,
      };

      lastUpdate.value = new Date();
    } catch (error) {
      console.error("Error loading system statistics:", error);
    } finally {
      loading.value = false;
    }
  };

  useEffect(() => {
    loadSystemStats();
    const interval = setInterval(loadSystemStats, 5 * 60 * 1000);
    return () => clearInterval(interval);
  }, []);

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat("pt-BR", {
      style: "currency",
      currency: "BRL",
    }).format(value);
  };

  const formatDistance = (value: number) => {
    return `${value.toFixed(2)} km`;
  };

  const formatPercentage = (value: number) => {
    return `${value.toFixed(1)}%`;
  };

  const getCompletionColor = (rate: number) => {
    if (rate >= 80) return "#4caf50";
    if (rate >= 60) return "#ff9800";
    return "#f44336";
  };

  const getTaskStatusColor = (type: string) => {
    switch (type) {
      case "completed":
        return "#4caf50";
      case "pending":
        return "#2196f3";
      case "overdue":
        return "#f44336";
      default:
        return "#666";
    }
  };

  if (loading.value && !stats.value) {
    return (
      <main style={{ width: "100%", height: "100%", padding: "1rem" }}>
        <ViewToolbar title="Dashboard do Sistema">
          <Group>
            <ProgressBar indeterminate />
          </Group>
        </ViewToolbar>
      </main>
    );
  }

  return (
    <main
      style={{
        width: "100%",
        height: "100%",
        padding: "1rem",
        overflow: "auto",
      }}
    >
      <ViewToolbar title="Dashboard do Sistema Indika">
        <Group>
          <Button
            onClick={loadSystemStats}
            theme="secondary"
            disabled={loading.value}
          >
            {loading.value ? "Atualizando..." : "Atualizar"}
          </Button>
          {lastUpdate.value && (
            <span style={{ fontSize: "0.85em", color: "#666" }}>
              Última atualização: {lastUpdate.value.toLocaleTimeString()}
            </span>
          )}
        </Group>
      </ViewToolbar>

      {stats.value && (
        <VerticalLayout
          theme="spacing"
          style={{ width: "100%", margin: "1rem 0" }}
        >
          {/* Quick Stats Cards */}
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fit, minmax(250px, 1fr))",
              gap: "1rem",
              width: "100%",
            }}
          >
            <Card style={{ padding: "1.5rem" }}>
              <h3 style={{ margin: "0 0 0.5rem 0", color: "#1976d2" }}>
                📋 Tarefas
              </h3>
              <div
                style={{
                  fontSize: "2rem",
                  fontWeight: "bold",
                  margin: "0.5rem 0",
                }}
              >
                {stats.value.tasks.total}
              </div>
              <div style={{ fontSize: "0.9em", color: "#666" }}>
                {stats.value.tasks.completed} concluídas •{" "}
                {stats.value.tasks.pending} pendentes
                {stats.value.tasks.overdue > 0 && (
                  <span style={{ color: "#f44336" }}>
                    {" "}
                    • {stats.value.tasks.overdue} vencidas
                  </span>
                )}
              </div>
            </Card>

            <Card style={{ padding: "1.5rem" }}>
              <h3 style={{ margin: "0 0 0.5rem 0", color: "#388e3c" }}>
                🏷️ Categorias
              </h3>
              <div
                style={{
                  fontSize: "2rem",
                  fontWeight: "bold",
                  margin: "0.5rem 0",
                }}
              >
                {stats.value.categorias}
              </div>
              <div style={{ fontSize: "0.9em", color: "#666" }}>
                Categorias de paragem
              </div>
            </Card>

            <Card style={{ padding: "1.5rem" }}>
              <h3 style={{ margin: "0 0 0.5rem 0", color: "#f57c00" }}>
                📍 Paragens
              </h3>
              <div
                style={{
                  fontSize: "2rem",
                  fontWeight: "bold",
                  margin: "0.5rem 0",
                }}
              >
                {stats.value.paragens}
              </div>
              <div style={{ fontSize: "0.9em", color: "#666" }}>
                Pontos de paragem
              </div>
            </Card>

            <Card style={{ padding: "1.5rem" }}>
              <h3 style={{ margin: "0 0 0.5rem 0", color: "#7b1fa2" }}>
                🛣️ Rotas
              </h3>
              <div
                style={{
                  fontSize: "2rem",
                  fontWeight: "bold",
                  margin: "0.5rem 0",
                }}
              >
                {stats.value.rotas}
              </div>
              <div style={{ fontSize: "0.9em", color: "#666" }}>
                Rotas cadastradas
              </div>
            </Card>
          </div>

          {/* Task Progress Section */}
          <Card style={{ padding: "1.5rem", margin: "1rem 0" }}>
            <h2 style={{ margin: "0 0 1rem 0" }}>📊 Progresso das Tarefas</h2>

            <div
              style={{
                display: "flex",
                alignItems: "center",
                gap: "2rem",
                flexWrap: "wrap",
              }}
            >
              <div style={{ flex: 1, minWidth: "300px" }}>
                <div style={{ marginBottom: "0.5rem" }}>
                  <strong>
                    Taxa de Conclusão:{" "}
                    {formatPercentage(stats.value.tasks.completionRate)}
                  </strong>
                </div>
                <ProgressBar
                  value={stats.value.tasks.completionRate / 100}
                  style={
                    {
                      width: "100%",
                      "--vaadin-progress-color": getCompletionColor(
                        stats.value.tasks.completionRate,
                      ),
                    } as React.CSSProperties
                  }
                />
              </div>

              <div style={{ display: "flex", gap: "2rem", flexWrap: "wrap" }}>
                <div style={{ textAlign: "center", minWidth: "80px" }}>
                  <div
                    style={{
                      fontSize: "1.2em",
                      fontWeight: "bold",
                      color: getTaskStatusColor("completed"),
                    }}
                  >
                    {stats.value.tasks.completed}
                  </div>
                  <div style={{ fontSize: "0.8em", color: "#666" }}>
                    Concluídas
                  </div>
                </div>
                <div style={{ textAlign: "center", minWidth: "80px" }}>
                  <div
                    style={{
                      fontSize: "1.2em",
                      fontWeight: "bold",
                      color: getTaskStatusColor("pending"),
                    }}
                  >
                    {stats.value.tasks.pending}
                  </div>
                  <div style={{ fontSize: "0.8em", color: "#666" }}>
                    Pendentes
                  </div>
                </div>
                {stats.value.tasks.overdue > 0 && (
                  <div style={{ textAlign: "center", minWidth: "80px" }}>
                    <div
                      style={{
                        fontSize: "1.2em",
                        fontWeight: "bold",
                        color: getTaskStatusColor("overdue"),
                      }}
                    >
                      {stats.value.tasks.overdue}
                    </div>
                    <div style={{ fontSize: "0.8em", color: "#666" }}>
                      Vencidas
                    </div>
                  </div>
                )}
              </div>
            </div>
          </Card>

          {/* Transport System Stats */}
          <HorizontalLayout theme="spacing" style={{ width: "100%" }}>
            <Card style={{ flex: 1, padding: "1.5rem" }}>
              <h3 style={{ margin: "0 0 1rem 0" }}>
                💰 Estatísticas de Preços
              </h3>
              {stats.value.precoStats ? (
                <VerticalLayout theme="spacing-xs">
                  <div>
                    <strong>Menor preço:</strong>{" "}
                    {formatCurrency(stats.value.precoStats.precoMinimo)}
                  </div>
                  <div>
                    <strong>Maior preço:</strong>{" "}
                    {formatCurrency(stats.value.precoStats.precoMaximo)}
                  </div>
                  <div>
                    <strong>Preço médio:</strong>{" "}
                    {formatCurrency(stats.value.precoStats.precoMedio)}
                  </div>
                </VerticalLayout>
              ) : (
                <div style={{ color: "#666", fontStyle: "italic" }}>
                  Nenhum dado de preço disponível
                </div>
              )}
            </Card>

            <Card style={{ flex: 1, padding: "1.5rem" }}>
              <h3 style={{ margin: "0 0 1rem 0" }}>
                📏 Estatísticas de Distâncias
              </h3>
              {stats.value.distanciaStats ? (
                <VerticalLayout theme="spacing-xs">
                  <div>
                    <strong>Menor distância:</strong>{" "}
                    {formatDistance(stats.value.distanciaStats.distanciaMinima)}
                  </div>
                  <div>
                    <strong>Maior distância:</strong>{" "}
                    {formatDistance(stats.value.distanciaStats.distanciaMaxima)}
                  </div>
                  <div>
                    <strong>Distância média:</strong>{" "}
                    {formatDistance(stats.value.distanciaStats.distanciaMedia)}
                  </div>
                </VerticalLayout>
              ) : (
                <div style={{ color: "#666", fontStyle: "italic" }}>
                  Nenhum dado de distância disponível
                </div>
              )}
            </Card>
          </HorizontalLayout>

          {/* System Status */}
          <Card style={{ padding: "1.5rem" }}>
            <h3 style={{ margin: "0 0 1rem 0" }}>🚦 Status do Sistema</h3>

            <HorizontalLayout theme="spacing" style={{ width: "100%" }}>
              <div style={{ flex: 1 }}>
                <div
                  style={{
                    padding: "1rem",
                    borderRadius: "8px",
                    backgroundColor:
                      stats.value.paragens > 0 ? "#e8f5e8" : "#fff3e0",
                    border: `1px solid ${stats.value.paragens > 0 ? "#4caf50" : "#ff9800"}`,
                    textAlign: "center",
                  }}
                >
                  <strong>Sistema de Transporte:</strong>
                  <br />
                  {stats.value.paragens > 0 && stats.value.rotas > 0
                    ? "✅ Operacional"
                    : "⚠️ Configuração Incompleta"}
                </div>
              </div>

              <div style={{ flex: 1 }}>
                <div
                  style={{
                    padding: "1rem",
                    borderRadius: "8px",
                    backgroundColor:
                      stats.value.tasks.total > 0 ? "#e3f2fd" : "#f5f5f5",
                    border: `1px solid ${stats.value.tasks.total > 0 ? "#2196f3" : "#ccc"}`,
                    textAlign: "center",
                  }}
                >
                  <strong>Gestão de Tarefas:</strong>
                  <br />
                  {stats.value.tasks.total > 0
                    ? `📝 ${stats.value.tasks.total} tarefas ativas`
                    : "📋 Nenhuma tarefa"}
                </div>
              </div>
            </HorizontalLayout>

            {/* Quick Actions */}
            <div style={{ marginTop: "1.5rem" }}>
              <h4>🚀 Ações Rápidas</h4>
              <HorizontalLayout theme="spacing" style={{ flexWrap: "wrap" }}>
                <Button
                  theme="primary"
                  onClick={() => (window.location.href = "/task-list")}
                >
                  Gerenciar Tarefas
                </Button>
                <Button
                  theme="secondary"
                  onClick={() => (window.location.href = "/paragem-list")}
                >
                  Cadastrar Paragens
                </Button>
                <Button
                  theme="secondary"
                  onClick={() => (window.location.href = "/rota-list")}
                >
                  Configurar Rotas
                </Button>
                <Button
                  theme="tertiary"
                  onClick={() =>
                    (window.location.href = "/categoria-paragem-list")
                  }
                >
                  Gerenciar Categorias
                </Button>
              </HorizontalLayout>
            </div>
          </Card>
        </VerticalLayout>
      )}
    </main>
  );
}
