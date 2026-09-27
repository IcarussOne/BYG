/**
 * Maps a biome temperature value (as used in BiomeProperties#setTemperature, roughly
 * -0.5 for icy biomes to 2.0 for the hottest deserts) to a hue on a cold-to-hot scale:
 * blue -> cyan -> green -> yellow -> orange -> red.
 */
export function tempColor(value: number): string {
  const min = -0.5;
  const max = 2.0;
  const t = Math.max(0, Math.min(1, (value - min) / (max - min)));
  const hue = 220 - t * 220;
  return `hsl(${hue}, 70%, 50%)`;
}
