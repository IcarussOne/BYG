import React from 'react';
import styles from './styles.module.css';

// Item icons live in src/assets/items/<item_registry_id>.png, copied from the mod's own
// 16x16 item textures. They're rendered upscaled with nearest-neighbour scaling so the
// pixel art stays crisp instead of blurring.
declare const require: {
  context: (dir: string, deep: boolean, filter: RegExp) => {
    keys: () => string[];
    (key: string): string | {default: string};
  };
};

const icons = require.context('../../assets/items', false, /\.png$/);
const woodIcons = require.context('../../assets/wood', false, /\.png$/);
const blockIcons = require.context('../../assets/blocks', false, /\.png$/);

function findIcon(id: string, source: 'items' | 'wood' | 'blocks'): string | undefined {
  const context = source === 'wood' ? woodIcons : source === 'blocks' ? blockIcons : icons;
  const key = `./${id}.png`;
  if (!context.keys().includes(key)) {
    return undefined;
  }
  const mod = context(key);
  return typeof mod === 'string' ? mod : mod.default;
}

interface ItemIconProps {
  id: string;
  name: string;
  /** Rendered size in pixels. Defaults to a large standalone card; pass a smaller value for inline/table use. */
  size?: number;
  /** Renders bare, without the card frame - for use inside a table cell or alongside text. */
  inline?: boolean;
  /** Asset collection for item sprites or verified block textures. */
  source?: 'items' | 'wood' | 'blocks';
}

export default function ItemIcon({id, name, size = 80, inline = false, source = 'items'}: ItemIconProps) {
  const src = findIcon(id, source);

  if (!src) {
    return inline ? (
      <span className={styles.missingInline} title={`Missing icon: ${id}.png`} />
    ) : (
      <div className={styles.placeholder} style={{width: size, height: size}}>
        Icon pending: <code>src/assets/{source}/{id}.png</code>
      </div>
    );
  }

  const img = (
    <img
      src={src}
      alt={name}
      width={size}
      height={size}
      loading="lazy"
      className={`${styles.pixelated} ${inline ? styles.inline : ''}`}
    />
  );

  if (inline) {
    return img;
  }

  return (
    <figure className={styles.card}>
      {img}
      <figcaption className={styles.caption}>{name}</figcaption>
    </figure>
  );
}
