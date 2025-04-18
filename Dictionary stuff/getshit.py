import requests
import os

# Replace with your raw GitHub URL
url = "https://raw.githubusercontent.com/broderickhyman/ao-bin-dumps/refs/heads/master/formatted/items.txt"

# Output path
output_path = r"C:\TEMP\stuff.txt"

try:
    response = requests.get(url)
    response.raise_for_status()

    content = response.text.strip()  # Example of basic formatting: remove leading/trailing whitespace

    # Ensure the directory exists
    os.makedirs(os.path.dirname(output_path), exist_ok=True)

    # Save the content
    with open(output_path, "w", encoding="utf-8") as f:
        f.write(content)
        print(f"Content saved to: {output_path}")

except requests.exceptions.RequestException as e:
    print(f"Error fetching data: {e}")
