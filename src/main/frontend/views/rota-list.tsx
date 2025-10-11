import { ViewConfig } from "@vaadin/hilla-file-router/types.js";
import {
  Button,
  Grid,
  GridColumn,
  TextField,
  Select,
  Dialog,
  HorizontalLayout,
  VerticalLayout,
  FormLayout,
  Card,
  NumberField,
  Tabs,
  Tab,
} from "@vaadin/react-components";
import { Notification } from "@vaadin/react-components/Notification";
import { RotaService, ParagemService } from "Frontend/generated/endpoints";
import { useSignal } from "@vaadin/hilla-react-signals";
import handleError from "Frontend/views/_ErrorHandler";
import { Group, ViewToolbar } from "Frontend/components/ViewToolbar";
import { useEffect } from "react";

export const config: ViewConfig = {
  title: "Rotas",
  menu: {
    icon: "vaadin:road",
    order: 4,
    title: "Rotas",
  },
};

interface Rota {
  paragemOrigemId: number;
  paragemDestinoId: number;
  preco: number;
  distancia: number;
  paragemOrigemLatitude?: number;
  paragemOrigemLongitude?: number;
  paragemDestinoLatitude?: number;
  paragemDestinoLongitude?: number;
  paragemOrigemCategoria?: string;
  paragemDestinoCategoria?: string;
}

interface Paragem {
  id: number;
  latitude: number;
  longitude: number;
  categoriaParagemId: number;
  categoriaParagemNome: string;
}

export default function RotaListView() {
  const rotas = useSignal<Rota[]>([]);
  const paragens = useSignal<Paragem[]>([]);
  const loading = useSignal(false);
  const dialogOpen = useSignal(false);
  const editingRota = useSignal<Rota | null>(null);
  const selectedTab = useSignal(0);

  // Statistics
  const precoStats = useSignal<any>(null);
  const distanciaStats = useSignal<any>(null);

  // Form fields
  const paragemOrigemId = useSignal<number | undefined>(undefined);
  const paragemDestinoId = useSignal<number | undefined>(undefined);
  const preco = useSignal<number | undefined>(undefined);
  const distancia = useSignal<number | undefined>(undefined);

  // Filter fields
  const precoMin = useSignal<number | undefined>(undefined);
  const precoMax = useSignal<number | undefined>(undefined);
  const distanciaMin = useSignal<number | undefined>(undefined);
  const distanciaMax = useSignal<number | undefined>(undefined);
  const selectedOrigem = useSignal<number | undefined>(undefined);
  const selectedDestino = useSignal<number | undefined>(undefined);

  const loadRotas = async () => {
    loading.value = true;
    try {
      const result = await RotaService.listarTodas();
      rotas.value = result;
    } catch (error) {
      handleError(error);
    } finally {
      loading.value = false;
    }
  };

  const loadParagens = async () => {
    try {
      const result = await ParagemService.listarTodas();
      paragens.value = result;
    } catch (error) {
      handleError(error);
    }
  };

  const loadStatistics = async () => {
    try {
      const [precoResult, distanciaResult] = await Promise.all([
        RotaService.obterEstatisticasPrecos(),
        RotaService.obterEstatisticasDistancias()
      ]);
      precoStats.value = precoResult;
      distanciaStats.value = distanciaResult;
    } catch (error) {
      console.error("Error loading statistics:", error);
    }
  };

  useEffect(() => {
    loadRotas();
    loadParagens();
    loadStatistics();
  }, []);

  const openCreateDialog = () => {
    editingRota.value = null;
    paragemOrigemId.value = undefined;
    paragemDestinoId.value = undefined;
    preco.value = undefined;
    distancia.value = undefined;
    dialogOpen.value = true;
  };

  const openEditDialog = (rota: Rota) => {
    editingRota.value = rota;
    paragemOrigemId.value = rota.paragemOrigemId;
    paragemDestinoId.value = rota.paragemDestinoId;
    preco.value = rota.preco;
    distancia.value = rota.distancia;
    dialogOpen.value = true;
  };

  const closeDialog = () => {
    dialogOpen.value = false;
    editingRota.value = null;
    paragemOrigemId.value = undefined;
    paragemDestinoId.value = undefined;
    preco.value = undefined;
    distancia.value = undefined;
  };

  const saveRota = async () => {
    if (!paragemOrigemId.value || !paragemDestinoId.value ||
        preco.value === undefined || distancia.value === undefined) {
      Notification.show("Todos os campos são obrigatórios", {
        duration: 3000,
        position: "bottom-end",
        theme: "error",
      });
      return;
    }

    if (paragemOrigemId.value === paragemDestinoId.value) {
      Notification.show("Paragem de origem deve ser diferente da paragem de destino", {
        duration: 3000,
        position: "bottom-end",
        theme: "error",
      });
      return;
    }

    if (preco.value <= 0 || distancia.value <= 0) {
      Notification.show("Preço e distância devem ser maiores que zero", {
        duration: 3000,
        position: "bottom-end",
        theme: "error",
      });
      return;
    }

    try {
      const rotaData = {
        paragemOrigemId: paragemOrigemId.value,
        paragemDestinoId: paragemDestinoId.value,
        preco: preco.value,
        distancia: distancia.value,
      };

      if (editingRota.value) {
        await RotaService.atualizar(
          editingRota.value.paragemOrigemId,
          editingRota.value.paragemDestinoId,
          rotaData
        );
        Notification.show("Rota atualizada com sucesso", {
          duration: 3000,
          position: "bottom-end",
          theme: "success",
        });
      } else {
        await RotaService.criar(rotaData);
        Notification.show("Rota criada com sucesso", {
          duration: 3000,
          position: "bottom-end",
          theme: "success",
        });
      }

      closeDialog();
      loadRotas();
      loadStatistics();
    } catch (error) {
      handleError(error);
    }
  };

  const deleteRota = async (rota: Rota) => {
    if (!confirm("Tem certeza que deseja excluir esta rota?")) {
      return;
    }

    try {
      await RotaService.deletar(rota.paragemOrigemId, rota.paragemDestinoId);
      Notification.show("Rota excluída com sucesso", {
        duration: 3000,
        position: "bottom-end",
        theme: "success",
      });
      loadRotas();
      loadStatistics();
    } catch (error) {
      handleError(error);
    }
  };

  const filterByPreco = async () => {
    if (precoMin.value !== undefined && precoMax.value !== undefined) {
      try {
        const results = await RotaService.buscarPorFaixaPreco(precoMin.value, precoMax.value);
        rotas.value = results;
        Notification.show(`${results.length} rotas encontradas na faixa de preço`, {
          duration: 3000,
          position: "bottom-end",
          theme: "success",
        });
      } catch (error) {
        handleError(error);
      }
    }
  };

  const filterByDistancia = async () => {
    if (distanciaMin.value !== undefined && distanciaMax.value !== undefined) {
      try {
        const results = await RotaService.buscarPorFaixaDistancia(distanciaMin.value, distanciaMax.value);
        rotas.value = results;
        Notification.show(`${results.length} rotas encontradas na faixa de distância`, {
          duration: 3000,
          position: "bottom-end",
          theme: "success",
        });
      } catch (error) {
        handleError(error);
      }
    }
  };

  const filterByOrigem = async () => {
    if (selectedOrigem.value) {
      try {
        const results = await RotaService.buscarPorOrigem(selectedOrigem.value);
        rotas.value = results;
        Notification.show(`${results.length} rotas partindo da paragem selecionada`, {
          duration: 3000,
          position: "bottom-end",
          theme: "success",
        });
      } catch (error) {
        handleError(error);
      }
    }
  };

  const filterByDestino = async () => {
    if (selectedDestino.value) {
      try {
        const results = await RotaService.buscarPorDestino(selectedDestino.value);
        rotas.value = results;
        Notification.show(`${results.length} rotas chegando à paragem selecionada`, {
          duration: 3000,
          position: "bottom-end",
          theme: "success",
        });
      } catch (error) {
        handleError(error);
      }
    }
  };

  const showMaisBaratas = async () => {
    try {
      const results = await RotaService.buscarMaisBaratas();
      rotas.value = results;
      Notification.show("Rotas ordenadas por preço crescente", {
        duration: 3000,
        position: "bottom-end",
        theme: "success",
      });
    } catch (error) {
      handleError(error);
    }
  };

  const showMaisCurtas = async () => {
    try {
      const results = await RotaService.buscarMaisCurtas();
      rotas.value = results;
      Notification.show("Rotas ordenadas por distância crescente", {
        duration: 3000,
        position: "bottom-end",
        theme: "success",
      });
    } catch (error) {
      handleError(error);
    }
  };

  const showMelhorCustoBeneficio = async () => {
    try {
      const results = await RotaService.buscarMelhorCustoBeneficio();
      rotas.value = results;
      Notification.show("Rotas ordenadas por melhor custo-benefício", {
        duration: 3000,
        position: "bottom-end",
        theme: "success",
      });
    } catch (error) {
      handleError(error);
    }
  };

  const clearFilters = () => {
    precoMin.value = undefined;
    precoMax.value = undefined;
    distanciaMin.value = undefined;
    distanciaMax.value = undefined;
    selectedOrigem.value = undefined;
    selectedDestino.value = undefined;
    loadRotas();
  };

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(value);
  };

  const formatDistance = (value: number) => {
    return `${value.toFixed(2)} km`;
  };

  const formatCostBenefit = (preco: number, distancia: number) => {
    const costPerKm = preco / distancia;
    return formatCurrency(costPerKm) + "/km";
  };

  const getParagemName = (id: number) => {
    const paragem = paragens.value.find(p => p.id === id);
    return paragem ? `#${paragem.id} (${paragem.categoriaParagemNome})` : `#${id}`;
  };

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m">
      <ViewToolbar title="Gestão de Rotas">
        <Group>
          <Button onClick={openCreateDialog} theme="primary">
            Nova Rota
          </Button>
        </Group>
      </ViewToolbar>

      {/* Statistics */}
      {(precoStats.value || distanciaStats.value) && (
        <Card>
          <VerticalLayout theme="spacing padding">
            <h3>Estatísticas das Rotas</h3>
            <HorizontalLayout theme="spacing">
              {precoStats.value && (
                <div>
                  <strong>Preços:</strong> {formatCurrency(precoStats.value.precoMinimo)} - {formatCurrency(precoStats.value.precoMaximo)}
                  (Média: {formatCurrency(precoStats.value.precoMedio)})
                </div>
              )}
              {distanciaStats.value && (
                <div>
                  <strong>Distâncias:</strong> {formatDistance(distanciaStats.value.distanciaMinima)} - {formatDistance(distanciaStats.value.distanciaMaxima)}
                  (Média: {formatDistance(distanciaStats.value.distanciaMedia)})
                </div>
              )}
            </HorizontalLayout>
          </VerticalLayout>
        </Card>
      )}

      {/* Filters */}
      <Card>
        <VerticalLayout theme="spacing padding">
          <h4>Filtros e Ordenação</h4>

          <Tabs
            selected={selectedTab.value}
            onSelectedChanged={(e) => selectedTab.value = e.detail.value}
          >
            <Tab>Filtros por Valor</Tab>
            <Tab>Filtros por Paragem</Tab>
            <Tab>Ordenação</Tab>
          </Tabs>

          {selectedTab.value === 0 && (
            <FormLayout>
              <HorizontalLayout theme="spacing">
                <NumberField
                  label="Preço Mínimo"
                  value={precoMin.value?.toString()}
                  onValueChanged={(e) => precoMin.value = e.detail.value ? parseFloat(e.detail.value) : undefined}
                  step={0.01}
                  min={0}
                />
                <NumberField
                  label="Preço Máximo"
                  value={precoMax.value?.toString()}
                  onValueChanged={(e) => precoMax.value = e.detail.value ? parseFloat(e.detail.value) : undefined}
                  step={0.01}
                  min={0}
                />
                <Button onClick={filterByPreco} theme="primary">
                  Filtrar por Preço
                </Button>
              </HorizontalLayout>

              <HorizontalLayout theme="spacing">
                <NumberField
                  label="Distância Mínima (km)"
                  value={distanciaMin.value?.toString()}
                  onValueChanged={(e) => distanciaMin.value = e.detail.value ? parseFloat(e.detail.value) : undefined}
                  step={0.01}
                  min={0}
                />
                <NumberField
                  label="Distância Máxima (km)"
                  value={distanciaMax.value?.toString()}
                  onValueChanged={(e) => distanciaMax.value = e.detail.value ? parseFloat(e.detail.value) : undefined}
                  step={0.01}
                  min={0}
                />
                <Button onClick={filterByDistancia} theme="primary">
                  Filtrar por Distância
                </Button>
              </HorizontalLayout>
            </FormLayout>
          )}

          {selectedTab.value === 1 && (
            <FormLayout>
              <HorizontalLayout theme="spacing">
                <Select
                  label="Paragem de Origem"
                  placeholder="Selecione uma paragem"
                  value={selectedOrigem.value?.toString()}
                  onValueChanged={(e) => selectedOrigem.value = e.detail.value ? parseInt(e.detail.value) : undefined}
                >
                  <option value="">Todas as origens</option>
                  {paragens.value.map((paragem) => (
                    <option key={paragem.id} value={paragem.id.toString()}>
                      {getParagemName(paragem.id)}
                    </option>
                  ))}
                </Select>
                <Button onClick={filterByOrigem} theme="primary">
                  Filtrar por Origem
                </Button>
              </HorizontalLayout>

              <HorizontalLayout theme="spacing">
                <Select
                  label="Paragem de Destino"
                  placeholder="Selecione uma paragem"
                  value={selectedDestino.value?.toString()}
                  onValueChanged={(e) => selectedDestino.value = e.detail.value ? parseInt(e.detail.value) : undefined}
                >
                  <option value="">Todos os destinos</option>
                  {paragens.value.map((paragem) => (
                    <option key={paragem.id} value={paragem.id.toString()}>
                      {getParagemName(paragem.id)}
                    </option>
                  ))}
                </Select>
                <Button onClick={filterByDestino} theme="primary">
                  Filtrar por Destino
                </Button>
              </HorizontalLayout>
            </FormLayout>
          )}

          {selectedTab.value === 2 && (
            <HorizontalLayout theme="spacing">
              <Button onClick={showMaisBaratas} theme="secondary">
                Mais Baratas
              </Button>
              <Button onClick={showMaisCurtas} theme="secondary">
                Mais Curtas
              </Button>
              <Button onClick={showMelhorCustoBeneficio} theme="secondary">
                Melhor Custo-Benefício
              </Button>
              <Button onClick={clearFilters} theme="tertiary">
                Limpar Filtros
              </Button>
            </HorizontalLayout>
          )}
        </VerticalLayout>
      </Card>

      {/* Grid */}
      <Card style={{ flexGrow: 1 }}>
        <Grid items={rotas.value} style={{ height: "100%" }}>
          <GridColumn header="Origem" flexGrow={2}>
            {({ item }) => (
              <div>
                <div><strong>#{item.paragemOrigemId}</strong></div>
                <div style={{ fontSize: "0.85em", color: "#666" }}>
                  {item.paragemOrigemCategoria}
                </div>
              </div>
            )}
          </GridColumn>

          <GridColumn header="Destino" flexGrow={2}>
            {({ item }) => (
              <div>
                <div><strong>#{item.paragemDestinoId}</strong></div>
                <div style={{ fontSize: "0.85em", color: "#666" }}>
                  {item.paragemDestinoCategoria}
                </div>
              </div>
            )}
          </GridColumn>

          <GridColumn header="Preço" flexGrow={1}>
            {({ item }) => (
              <span style={{ fontWeight: "bold", color: "#1976d2" }}>
                {formatCurrency(item.preco)}
              </span>
            )}
          </GridColumn>

          <GridColumn header="Distância" flexGrow={1}>
            {({ item }) => formatDistance(item.distancia)}
          </GridColumn>

          <GridColumn header="Custo/km" flexGrow={1}>
            {({ item }) => (
              <span style={{ fontSize: "0.9em", color: "#666" }}>
                {formatCostBenefit(item.preco, item.distancia)}
              </span>
            )}
          </GridColumn>

          <GridColumn header="Coordenadas" flexGrow={2}>
            {({ item }) => (
              <div style={{ fontSize: "0.8em", fontFamily: "monospace" }}>
                <div>Origem: {item.paragemOrigemLatitude?.toFixed(4)}, {item.paragemOrigemLongitude?.toFixed(4)}</div>
                <div>Destino: {item.paragemDestinoLatitude?.toFixed(4)}, {item.paragemDestinoLongitude?.toFixed(4)}</div>
              </div>
            )}
          </GridColumn>

          <GridColumn header="Ações" width="200px" flexGrow={0}>
            {({ item }) => (
              <HorizontalLayout theme="spacing">
                <Button
                  theme="primary tertiary small"
                  onClick={() => openEditDialog(item)}
                >
                  Editar
                </Button>
                <Button
                  theme="error tertiary small"
                  onClick={() => deleteRota(item)}
                >
                  Excluir
                </Button>
              </HorizontalLayout>
            )}
          </GridColumn>
        </Grid>
      </Card>

      {/* Dialog */}
      <Dialog
        opened={dialogOpen.value}
        onOpenedChanged={(e) => {
          if (!e.detail.value) closeDialog();
        }}
        headerTitle={editingRota.value ? "Editar Rota" : "Nova Rota"}
      >
        <VerticalLayout theme="spacing" style={{ width: "500px" }}>
          <FormLayout>
            <Select
              label="Paragem de Origem"
              value={paragemOrigemId.value?.toString()}
              onValueChanged={(e) => paragemOrigemId.value = e.detail.value ? parseInt(e.detail.value) : undefined}
              required
              disabled={!!editingRota.value}
            >
              <option value="">Selecione a origem</option>
              {paragens.value.map((paragem) => (
                <option key={paragem.id} value={paragem.id.toString()}>
                  {getParagemName(paragem.id)}
                </option>
              ))}
            </Select>

            <Select
              label="Paragem de Destino"
              value={paragemDestinoId.value?.toString()}
              onValueChanged={(e) => paragemDestinoId.value = e.detail.value ? parseInt(e.detail.value) : undefined}
              required
              disabled={!!editingRota.value}
            >
              <option value="">Selecione o destino</option>
              {paragens.value
                .filter(paragem => paragem.id !== paragemOrigemId.value)
                .map((paragem) => (
                  <option key={paragem.id} value={paragem.id.toString()}>
                    {getParagemName(paragem.id)}
                  </option>
                ))}
            </Select>

            <NumberField
              label="Preço (R$)"
              value={preco.value?.toString()}
              onValueChanged={(e) => preco.value = e.detail.value ? parseFloat(e.detail.value) : undefined}
              required
              step={0.01}
              min={0.01}
              helperText="Valor deve ser maior que zero"
            />

            <NumberField
              label="Distância (km)"
              value={distancia.value?.toString()}
              onValueChanged={(e) => distancia.value = e.detail.value ? parseFloat(e.detail.value) : undefined}
              required
              step={0.01}
              min={0.01}
              helperText="Distância deve ser maior que zero"
            />

            {preco.value && distancia.value && preco.value > 0 && distancia.value > 0 && (
              <div style={{ padding: "8px", backgroundColor: "#f5f5f5", borderRadius: "4px" }}>
                <strong>Custo-benefício:</strong> {formatCostBenefit(preco.value, distancia.value)}
              </div>
            )}
          </FormLayout>

          <HorizontalLayout theme="spacing" style={{ justifyContent: "flex-end" }}>
            <Button onClick={closeDialog} theme="tertiary">
              Cancelar
            </Button>
            <Button onClick={saveRota} theme="primary">
              {editingRota.value ? "Atualizar" : "Criar"}
            </Button>
          </HorizontalLayout>
        </VerticalLayout>
      </Dialog>
    </main>
  );
}
