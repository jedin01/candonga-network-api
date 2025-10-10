import { Notification } from '@vaadin/react-components/Notification';

export default function handleError(error: any) {
  console.error('An unexpected error occurred', error);
  Notification.show('An unexpected error occurred. Please try again later.', {
    duration: 3000,
    position: 'top-center',
    theme: 'error',
  });
}
