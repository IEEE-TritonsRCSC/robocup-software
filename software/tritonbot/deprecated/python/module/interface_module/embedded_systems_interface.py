import usb.core
import usb.util

vendor_id = 0x1234 # Replace with actual vendor ID
product_id = 0x9678

device = usb.core.find(idVendor = vendor_id, idProduct = product_id)
if device is None:
    raise ValueError('Device not found')

configuration = device.get_active_configuration()
interface = configuration[(0, 0)]
endpoint = interface[0]

#Send a command to the controller
command = b'\x01\x02\x03'
endpoint.write(command)

# Receive a response from the controler
response = endpoint.read(endpoint.wMaxPacketSize)
