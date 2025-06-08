import re
import shutil

tiers_to_remove = r"(Elder's|Master's|Adept's|Expert's|Grandmaster's|Uncommon|Rare|Exceptional|Pristine|Worked|Cured|Hardened|Reinforced|Fortified|Major|Minor|)"

# Function to check if a line matches the specified pattern
def check_line(line):
    # Regular expression to match the pattern
    pattern = r'.*T[4-8]_.*@[1-4].*'
    
    # Check if the line matches the pattern   

    if re.match(pattern, line.strip()):
        return True
    return False

def create_backup(input_file):
    # Create a backup of the input file
    backup_file = 'C:/TEMP/dictionary_backup.txt'
    shutil.copy(input_file, backup_file)
    print(f"Backup created: {backup_file}")

def process_file(input_file, output_file):
    # Open the input file and output file
    last_line=None
    last_line2=None
    with open(input_file, 'r', encoding='utf-8') as infile, open(output_file, 'w') as outfile:
        for line in infile:
            trimmed_line = line.strip()  # Trim leading/trailing whitespace
            if check_line(trimmed_line):  # Keep only lines that match the pattern
                cleaned_line = re.sub(r'\b(?:[0-9]{1,4}|10000):|T[4-8]_|\@[1-4]', '', trimmed_line)
                cleaned_line = re.sub(r'(\s{2,})','', cleaned_line.strip())
                #cleaned_line = re.sub(r':',',',cleaned_line)
                if cleaned_line != last_line:
                    cleaned_line = re.sub(tiers_to_remove,'',cleaned_line)
                    cleaned_line = re.sub(r':\s*',',', cleaned_line.strip())
                    #print(cleaned_line)
                    last_line = cleaned_line
                  #  if ':' in cleaned_line:
                   #     value, key = cleaned_line.strip().split(',',1)
                    #    cleaned_line=f'"{key}":"{value}",'                        
                    if cleaned_line != last_line2:
                        outfile.write(f"{cleaned_line}\n")            
                        last_line2=cleaned_line               
                    #else:
                        #print(f"Deleted line: {trimmed_line}")  # Print deleted lines for reference

# Define input and output file paths
input_file = 'C:/TEMP/dictionary.txt'  # Replace with your actual file path
output_file = 'C:/TEMP/dictionary2.txt'

# Create a backup before modifying the file
#create_backup(input_file)

# Run the processing function
process_file(input_file, output_file)

print(f"Updated file saved as {output_file}")
