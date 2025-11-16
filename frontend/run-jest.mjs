import { spawn } from 'child_process';
import { fileURLToPath } from 'url';
import { dirname, join } from 'path';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

const jestPath = join(__dirname, 'node_modules', '.bin', 'jest.cmd');

const jest = spawn(jestPath, ['--coverage'], {
  cwd: __dirname,
  stdio: 'inherit',
  shell: true
});

jest.on('error', (error) => {
  console.error('Failed to start Jest:', error);
  process.exit(1);
});

jest.on('close', (code) => {
  console.log(`\nJest process exited with code ${code}`);
  process.exit(code);
});
