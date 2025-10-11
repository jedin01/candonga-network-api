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
} from "@vaadin/react-components";
import { Notification } from "@vaadin/react-components/Notification";
import { ParagemService, CategoriaParagemService } from "Frontend/generated/endpoints";
import { useSignal } from "@vaadin/hilla-react-signals";
import handleError from "Frontend/views/_ErrorHandler";
import { Group, ViewToolbar } from "Frontend/components/ViewToolbar";
import { useEffect } from "react";

export const config: ViewConfig = {
  title: "Paragens",
  menu: {
    icon: "vaadin:map-marker",
    order: 3,
    title: "Paragens",
  },
};

interface Paragem {
  id?: number;
  latitude: number;
  longitude: number;
  categoriaParagemId: number;
  categoriaParagemNome?: string;
}

interface CategoriaParagem {
  id: number;
  nome: string;
  descricao?: string;
}

export default function ParagemListView() {
  const paragens = useSignal<Paragem[]>([]);
  const categorias = useSignal<CategoriaParagem[]>([]);
  const loading = useSignal(false);
  const dialogOpen = useSignal(false);
  const editingParagem = useSignal<Paragem | null>(null);
  const searchTerm = useSignal("");
  const selectedCategoria = useSignal<number | null>(null);
  const statistics = useSignal<any[]>([]);

  // Form fields
  const latitude = useSignal<number | undefined>(undefined);
  const longitude = useSignal<number | undefined>(undefined);
  const categoriaParagemId = useSignal<number | undefined>(undefined);

  // Area search fields
  const areaSearch = useSignal(false);
  const latMin = useSignal<number | undefined>(undefined);
  const latMax = useSignal<number | undefined>(undefined);
  const longMin = useSignal<number | undefined>(undefined);
  const longMax = useSignal<number | undefined>(undefined);

  const loadParagens = async () => {
    loading.value = true;
    try {
      const result = await ParagemService.listarTodas();
      paragens.value = result;
    } catch (error) {
      handleError(error);
    } finally {
      loading.value = false;
    }
  };

  const loadCategorias = async () => {
    try {
      const result = await CategoriaParagemService.listarTodas();
      categorias.value = result;
    } catch (error) {
      handleError(error);
    }
  };

  const loadStatistics = async () => {
    try {
      const stats = await ParagemService.obterEstatisticas();
      statistics.value = stats;
    } catch (error) {
      console.error("Error loading statistics:", error);
    }
  };

  useEffect(() => {
    loadParagens();
    loadCategorias();
    loadStatistics();
  }, []);

  const openCreateDialog = () => {
    editingParagem.value = null;
    latitude.value = undefined;
    longitude.value = undefined;
    categoriaParagemId.value = undefined;
    dialogOpen.value = true;
  };

  const openEditDialog = (paragem: Paragem) => {
    editingParagem.value = paragem;
    latitude.value = paragem.latitude;
    longitude.value = paragem.longitude;
    categoriaParagemId.value = paragem.categoriaParagemId;
    dialogOpen.value = true;
  };

  const closeDialog = () => {
    dialogOpen.value = false;
    editingParagem.value = null;
    latitude.value = undefined;
    longitude.value = undefined;
    categoriaParagemId.value = undefined;
  };

  const saveParagem = async () => {
    if (latitude.value === undefined || longitude.value === undefined || !categoriaParagemId.value) {
      Notification.show("Todos os campos são obrigatórios", {
        duration: 3000,
        position: "bottom-end",
        theme: "error",
      });
      return;
    }

    if (latitude.value < -90 || latitude.value > 90) {
      Notification.show("Latitude deve estar entre -90 e 90", {
        duration: 3000,
        position: "bottom-end",
        theme: "error",
      });
      return;
    }

    if (longitude.value < -180 || longitude.value > 180) {
      Notification.show("Longitude deve estar entre -180 e 180", {
        duration: 3000,
        position: "bottom-end",
        theme: "error",
      });
      return;
    }

    try {
      const paragemData = {
        latitude: latitude.value,
        longitude: longitude.value,
        categoriaParagemId: categoriaParagemId.value,
      };

      if (editingParagem.value) {
        await ParagemService.atualizar(editingParagem.value.id!, paragemData);
        Notification.show("Paragem atualizada com sucesso", {
          duration: 3000,
          position: "bottom-end",
          theme: "success",
        });
      } else {
        await ParagemService.criar(paragemData);
        Notification.show("Paragem criada com sucesso", {
          duration: 3000,
          position: "bottom-end",
          theme: "success",
        });
      }

      closeDialog();
      loadParagens();
      loadStatistics();
    } catch (error) {
      handleError(error);
    }
  };

  const deleteParagem = async (paragem: Paragem) => {
    if (!confirm(`Tem certeza que deseja excluir esta paragem?`)) {
      return;
    }

    try {
      await ParagemService.deletar(paragem.id!);
      Notification.show("Paragem excluída com sucesso", {
        duration: 3000,
        position: "bottom-end",
        theme: "success",
      });
      loadParagens();
      loadStatistics();
    } catch (error) {
      handleError(error);
    }
  };

  const filterByCategoria = async () => {
    if (selectedCategoria.value) {
      try {
        const results = await ParagemService.buscarPorCategoria(selectedCategoria.value);
        paragens.value = results;
      } catch (error) {
        handleError(error);
      }
    } else {
      loadParagens();
    }
  };

  const searchByArea = async () => {
    if (latMin.value !== undefined && latMax.value !== undefined &&
        longMin.value !== undefined && longMax.value !== undefined) {
      try {
        const results = await ParagemService.buscarPorArea(latMin.value, latMax.value, longMin.value, longMax.value);
        paragens.value = results;
        Notification.show(`Encontradas ${results.length} paragens na área`, {
          duration: 3000,
          position: "bottom-end",
          theme: "success",
        });
      } catch (error) {
        handleError(error);
      }
    } else {
      Notification.show("Preencha todas as coordenadas da área", {
        duration: 3000,
        position: "bottom-end",
        theme: "error",
      });
    }
  };

  const showConectadas = async () => {
    try {
      const results = await ParagemService.buscarConectadas();
      paragens.value = results;
      Notification.show(`${results.length} paragens conectadas`, {
        duration: 3000,
        position: "bottom-end",
        theme: "success",
      });
    } catch (error) {
      handleError(error);
    }
  };

  const showIsoladas = async () => {
    try {
      const results = await ParagemService.buscarIsoladas();
      paragens.value = results;
      Notification.show(`${results.length} paragens isoladas`, {
        duration: 3000,
        position: "bottom-end",
        theme: "warning",
      });
    } catch (error) {
      handleError(error);
    }
  };

  const clearFilters = () => {
    selectedCategoria.value = null;
    latMin.value = undefined;
    latMax.value = undefined;
    longMin.value = undefined;
    longMax.value = undefined;
    loadParagens();
  };

  const formatCoordinate = (value: number, type: 'lat' | 'lng') => {
    const direction = type === 'lat' ? (value >= 0 ? 'N' : 'S') : (value >= 0 ? 'E' : 'W');
    return `${Math.abs(value).toFixed(6)}° ${direction}`;
  };

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m">
      <ViewToolbar title="Gestão de Paragens">
        <Group>
          <Button onClick={openCreateDialog} theme="primary">
            Nova Paragem
          </Button>
        </Group>
      </ViewToolbar>

      {/* Statistics */}
      {statistics.value.length > 0 && (
        <Card>
          <VerticalLayout theme="spacing padding">
            <h3>Estatísticas de Paragens por Categoria</h3>
            <HorizontalLayout theme="spacing">
              {statistics.value.map((stat, index) => (
                <div key={index}>
                  <strong>{stat.nomeCategoria}:</strong> {stat.quantidadeParagens} paragens
                </div>
              ))}
            </HorizontalLayout>
          </VerticalLayout>
        </Card>
      )}

      {/* Filters */}
      <Card>
        <VerticalLayout theme="spacing padding">
          <h4>Filtros e Busca</h4>

          {/* Category Filter */}
          <HorizontalLayout theme="spacing">
            <Select
              label="Filtrar por Categoria"
              placeholder="Todas as categorias"
              value={selectedCategoria.value?.toString()}
              onValueChanged={(e) => {
                selectedCategoria.value = e.detail.value ? parseInt(e.detail.value) : null;
                filterByCategoria();
              }}
            >
              <option value="">Todas as categorias</option>
              {categorias.value.map((cat) => (
                <option key={cat.id} value={cat.id.toString()}>
                  {cat.nome}
                </option>
              ))}
            </Select>

            <Button onClick={showConectadas} theme="secondary">
              Paragens Conectadas
            </Button>
            <Button onClick={showIsoladas} theme="secondary">
              Paragens Isoladas
            </Button>
            <Button onClick={clearFilters} theme="tertiary">
              Limpar Filtros
            </Button>
          </HorizontalLayout>

          {/* Area Search */}
          <HorizontalLayout theme="spacing">
            <Button
              onClick={() => areaSearch.value = !areaSearch.value}
              theme={areaSearch.value ? "primary" : "secondary"}
            >
              {areaSearch.value ? "Ocultar" : "Busca por Área"}
            </Button>
          </HorizontalLayout>

          {areaSearch.value && (
            <FormLayout>
              <HorizontalLayout theme="spacing">
                <NumberField
                  label="Latitude Mín"
                  value={latMin.value?.toString()}
                  onValueChanged={(e) => latMin.value = e.detail.value ? parseFloat(e.detail.value) : undefined}
                  step={0.000001}
                  min={-90}
                  max={90}
                />
                <NumberField
                  label="Latitude Máx"
                  value={latMax.value?.toString()}
                  onValueChanged={(e) => latMax.value = e.detail.value ? parseFloat(e.detail.value) : undefined}
                  step={0.000001}
                  min={-90}
                  max={90}
                />
                <NumberField
                  label="Longitude Mín"
                  value={longMin.value?.toString()}
                  onValueChanged={(e) => longMin.value = e.detail.value ? parseFloat(e.detail.value) : undefined}
                  step={0.000001}
                  min={-180}
                  max={180}
                />
                <NumberField
                  label="Longitude Máx"
                  value={longMax.value?.toString()}
                  onValueChanged={(e) => longMax.value = e.detail.value ? parseFloat(e.detail.value) : undefined}
                  step={0.000001}
                  min={-180}
                  max={180}
                />
                <Button onClick={searchByArea} theme="primary">
                  Buscar Área
                </Button>
              </HorizontalLayout>
            </FormLayout>
          )}
        </VerticalLayout>
      </Card>

      {/* Grid */}
      <Card style={{ flexGrow: 1 }}>
        <Grid items={paragens.value} style={{ height: "100%" }}>
          <GridColumn path="id" header="ID" width="80px" flexGrow={0} />

          <GridColumn path="latitude" header="Latitude" flexGrow={1}>
            {({ item }) => formatCoordinate(item.latitude, 'lat')}
          </GridColumn>

          <GridColumn path="longitude" header="Longitude" flexGrow={1}>
            {({ item }) => formatCoordinate(item.longitude, 'lng')}
          </GridColumn>

          <GridColumn path="categoriaParagemNome" header="Categoria" flexGrow={1}>
            {({ item }) => (
              <span style={{
                padding: "2px 8px",
                borderRadius: "4px",
                backgroundColor: "#e3f2fd",
                color: "#1976d2"
              }}>
                {item.categoriaParagemNome}
              </span>
            )}
          </GridColumn>

          <GridColumn header="Coordenadas" flexGrow={2}>
            {({ item }) => (
              <code style={{ fontSize: "0.85em", color: "#666" }}>
                {item.latitude.toFixed(6)}, {item.longitude.toFixed(6)}
              </code>
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
                  onClick={() => deleteParagem(item)}
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
        headerTitle={editingParagem.value ? "Editar Paragem" : "Nova Paragem"}
      >
        <VerticalLayout theme="spacing" style={{ width: "450px" }}>
          <FormLayout>
            <NumberField
              label="Latitude"
              value={latitude.value?.toString()}
              onValueChanged={(e) => latitude.value = e.detail.value ? parseFloat(e.detail.value) : undefined}
              required
              step={0.000001}
              min={-90}
              max={90}
              helperText="Entre -90 e 90 graus"
            />

            <NumberField
              label="Longitude"
              value={longitude.value?.toString()}
              onValueChanged={(e) => longitude.value = e.detail.value ? parseFloat(e.detail.value) : undefined}
              required
              step={0.000001}
              min={-180}
              max={180}
              helperText="Entre -180 e 180 graus"
            />

            <Select
              label="Categoria da Paragem"
              value={categoriaParagemId.value?.toString()}
              onValueChanged={(e) => categoriaParagemId.value = e.detail.value ? parseInt(e.detail.value) : undefined}
              required
            >
              <option value="">Selecione uma categoria</option>
              {categorias.value.map((cat) => (
                <option key={cat.id} value={cat.id.toString()}>
                  {cat.nome} {cat.descricao && `- ${cat.descricao}`}
                </option>
              ))}
            </Select>
          </FormLayout>

          <HorizontalLayout theme="spacing" style={{ justifyContent: "flex-end" }}>
            <Button onClick={closeDialog} theme="tertiary">
              Cancelar
            </Button>
            <Button onClick={saveParagem} theme="primary">
              {editingParagem.value ? "Atualizar" : "Criar"}
            </Button>
          </HorizontalLayout>
        </VerticalLayout>
      </Dialog>
    </main>
  );
}
