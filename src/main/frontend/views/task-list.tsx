import { ViewConfig } from "@vaadin/hilla-file-router/types.js";
import {
  Button,
  DatePicker,
  Grid,
  GridColumn,
  TextField,
  Checkbox,
  Select,
  HorizontalLayout,
  VerticalLayout,
  FormLayout,
  Card,
} from "@vaadin/react-components";
import { Notification } from "@vaadin/react-components/Notification";
import { TaskService } from "Frontend/generated/endpoints";
import { useSignal } from "@vaadin/hilla-react-signals";
import handleError from "Frontend/views/_ErrorHandler";
import { Group, ViewToolbar } from "Frontend/components/ViewToolbar";
import { useGridDataProvider } from "@vaadin/hilla-react-crud";
import { useEffect } from "react";

export const config: ViewConfig = {
  title: "Task List",
  menu: {
    icon: "vaadin:clipboard-check",
    order: 1,
    title: "Task List",
  },
};

const dateTimeFormatter = new Intl.DateTimeFormat(undefined, {
  dateStyle: "medium",
  timeStyle: "medium",
});

const dateFormatter = new Intl.DateTimeFormat(undefined, {
  dateStyle: "medium",
});

type TaskEntryFormProps = {
  onTaskCreated?: () => void;
};

function TaskEntryForm(props: TaskEntryFormProps) {
  const description = useSignal("");
  const dueDate = useSignal<string | undefined>("");

  const createTask = async () => {
    if (!description.value.trim()) {
      Notification.show("Task description is required", {
        duration: 3000,
        position: "bottom-end",
        theme: "error",
      });
      return;
    }

    try {
      await TaskService.createTask(description.value, dueDate.value);
      if (props.onTaskCreated) {
        props.onTaskCreated();
      }
      description.value = "";
      dueDate.value = undefined;
      Notification.show("Task created successfully", {
        duration: 3000,
        position: "bottom-end",
        theme: "success",
      });
    } catch (error) {
      handleError(error);
    }
  };

  const handleKeyPress = (event: KeyboardEvent) => {
    if (event.key === "Enter" && !event.shiftKey) {
      event.preventDefault();
      createTask();
    }
  };

  return (
    <FormLayout>
      <HorizontalLayout theme="spacing">
        <TextField
          placeholder="What do you want to do?"
          aria-label="Task description"
          maxlength={255}
          style={{ minWidth: "25em" }}
          value={description.value}
          onValueChanged={(evt) => (description.value = evt.detail.value)}
          onKeyPress={handleKeyPress}
          required
        />
        <DatePicker
          placeholder="Due date (optional)"
          aria-label="Due date"
          value={dueDate.value}
          onValueChanged={(evt) => (dueDate.value = evt.detail.value)}
        />
        <Button
          onClick={createTask}
          theme="primary"
          disabled={!description.value.trim()}
        >
          Create Task
        </Button>
      </HorizontalLayout>
    </FormLayout>
  );
}

export default function TaskListView() {
  const dataProvider = useGridDataProvider(TaskService.list);
  const filterType = useSignal("all");
  const searchTerm = useSignal("");
  const statistics = useSignal<any>(null);

  const loadStatistics = async () => {
    try {
      const stats = await TaskService.getStatistics();
      statistics.value = stats;
    } catch (error) {
      console.error("Error loading statistics:", error);
    }
  };

  useEffect(() => {
    loadStatistics();
  }, []);

  const toggleTaskCompletion = async (task: any) => {
    try {
      if (task.completed) {
        await TaskService.markAsIncomplete(task.id);
      } else {
        await TaskService.markAsCompleted(task.id);
      }
      dataProvider.refresh();
      loadStatistics();
      Notification.show(
        `Task marked as ${task.completed ? "incomplete" : "completed"}`,
        { duration: 2000, position: "bottom-end", theme: "success" },
      );
    } catch (error) {
      handleError(error);
    }
  };

  const deleteTask = async (taskId: number) => {
    if (confirm("Are you sure you want to delete this task?")) {
      try {
        await TaskService.deleteTask(taskId);
        dataProvider.refresh();
        loadStatistics();
        Notification.show("Task deleted successfully", {
          duration: 2000,
          position: "bottom-end",
          theme: "success",
        });
      } catch (error) {
        handleError(error);
      }
    }
  };

  const searchTasks = async () => {
    if (searchTerm.value.trim()) {
      try {
        const results = await TaskService.searchByDescription(searchTerm.value);
        // Handle search results - you might want to update the grid data
        console.log("Search results:", results);
      } catch (error) {
        handleError(error);
      }
    }
  };

  const formatDueDate = (dueDate: string) => {
    if (!dueDate) return "No due date";
    const date = new Date(dueDate);
    const today = new Date();
    const isOverdue = date < today;
    const isDueToday = date.toDateString() === today.toDateString();

    let className = "";
    if (isOverdue) className = "text-error";
    else if (isDueToday) className = "text-warning";

    return (
      <span className={className}>
        {dateFormatter.format(date)}
        {isOverdue && " (Overdue)"}
        {isDueToday && " (Due Today)"}
      </span>
    );
  };

  const onTaskCreated = () => {
    dataProvider.refresh();
    loadStatistics();
  };

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m">
      <ViewToolbar title="Task Management">
        <Group>
          <TaskEntryForm onTaskCreated={onTaskCreated} />
        </Group>
      </ViewToolbar>

      {/* Statistics Card */}
      {statistics.value && (
        <Card>
          <HorizontalLayout theme="spacing padding">
            <div>
              <strong>Total Tasks: </strong>
              {statistics.value.totalTasks}
            </div>
            <div>
              <strong>Completed: </strong>
              {statistics.value.completedTasks}
            </div>
            <div>
              <strong>Pending: </strong>
              {statistics.value.pendingTasks}
            </div>
            <div>
              <strong>Overdue: </strong>
              <span className="text-error">
                {statistics.value.overdueTasks}
              </span>
            </div>
            <div>
              <strong>Completion Rate: </strong>
              {statistics.value.completionRate?.toFixed(1)}%
            </div>
          </HorizontalLayout>
        </Card>
      )}

      {/* Filters and Search */}
      <Card>
        <HorizontalLayout theme="spacing padding">
          <Select
            label="Filter Tasks"
            value={filterType.value}
            onValueChanged={(e) => (filterType.value = e.detail.value)}
          >
            <option value="all">All Tasks</option>
            <option value="pending">Pending</option>
            <option value="completed">Completed</option>
            <option value="overdue">Overdue</option>
            <option value="due-today">Due Today</option>
          </Select>

          <TextField
            placeholder="Search tasks..."
            value={searchTerm.value}
            onValueChanged={(e) => (searchTerm.value = e.detail.value)}
            style={{ minWidth: "20em" }}
          />

          <Button onClick={searchTasks} theme="secondary">
            Search
          </Button>
        </HorizontalLayout>
      </Card>

      {/* Tasks Grid */}
      <Grid dataProvider={dataProvider} style={{ flexGrow: 1 }}>
        <GridColumn width="50px" flexGrow={0}>
          {({ item }) => (
            <Checkbox
              checked={item.completed}
              onCheckedChanged={() => toggleTaskCompletion(item)}
            />
          )}
        </GridColumn>

        <GridColumn path="description" header="Task Description" flexGrow={3}>
          {({ item }) => (
            <span
              style={{
                textDecoration: item.completed ? "line-through" : "none",
                opacity: item.completed ? 0.7 : 1,
              }}
            >
              {item.description}
            </span>
          )}
        </GridColumn>

        <GridColumn path="dueDate" header="Due Date" flexGrow={1}>
          {({ item }) => formatDueDate(item.dueDate)}
        </GridColumn>

        <GridColumn path="creationDate" header="Created" flexGrow={1}>
          {({ item }) => dateTimeFormatter.format(new Date(item.creationDate))}
        </GridColumn>

        <GridColumn header="Status" width="100px" flexGrow={0}>
          {({ item }) => (
            <span className={item.completed ? "text-success" : "text-primary"}>
              {item.completed ? "Completed" : "Pending"}
            </span>
          )}
        </GridColumn>

        <GridColumn header="Actions" width="120px" flexGrow={0}>
          {({ item }) => (
            <HorizontalLayout theme="spacing">
              <Button
                theme="error tertiary small"
                onClick={() => deleteTask(item.id)}
              >
                Delete
              </Button>
            </HorizontalLayout>
          )}
        </GridColumn>
      </Grid>
    </main>
  );
}
